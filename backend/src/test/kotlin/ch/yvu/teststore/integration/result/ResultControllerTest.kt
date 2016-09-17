package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class ResultControllerTest : BaseIntegrationTest() {

    companion object {
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val nowString = SimpleDateFormat(isoFormat).format(Date())
        val now = SimpleDateFormat(isoFormat).parse(nowString)
    }

    @Autowired lateinit var resultRepository: ResultRepository

    @Autowired lateinit var runRepository: RunRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun createResultReturnsCorrectStatusCode() {
        given().queryParam("run", randomUUID())
                .queryParam("testName", "MyTest")
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .queryParam("durationMillis", 10)
                .queryParam("time", nowString)
                .post("/results")
                .then().assertThat().statusCode(201)
    }

    @Test fun createResultStoresResultWithCorrectAttributes() {
        val run = randomUUID()
        val testName = "MyTest"
        val retryNum = 0
        val passed = true
        val durationMillis = 10L
        given().queryParam("run", run)
                .queryParam("testName", testName)
                .queryParam("retryNum", retryNum)
                .queryParam("passed", passed)
                .queryParam("durationMillis", durationMillis)
                .queryParam("time", nowString)
                .post("/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(
                equalTo(run), testName, retryNum, passed, durationMillis, now, null)))
    }

    @Test fun createResultCanCreateFailedResult() {
        val run = randomUUID()
        val testName = "MyTest"
        val retryNum = 0
        val passed = false
        val durationMillis = 10L
        val stackTrace = "stacktrace";
        given().queryParam("run", run)
                .queryParam("testName", testName)
                .queryParam("retryNum", retryNum)
                .queryParam("passed", passed)
                .queryParam("durationMillis", durationMillis)
                .queryParam("time", nowString)
                .queryParam("stackTrace", stackTrace)
                .post("/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(
                equalTo(run), testName, retryNum, passed, durationMillis, now, stackTrace)))
    }

    @Test fun findAllByRun() {
        val runId = randomUUID()

        val result1 = Result(runId, "MyTest", 0, true, 204, Date())
        resultRepository.save(result1)

        val result2 = Result(runId, "MyTest2", 0, true, 478, Date())
        resultRepository.save(result2)

        given()
                .get("/runs/$runId/results")
                .then()
                .statusCode(200)
                .body("[0].testName", equalTo(result1.testName))
                .body("[1].testName", equalTo(result2.testName))
    }

    @Test fun findAllByRunGrouped() {
        val runId = randomUUID()

        val resultFlakyTest = Result(runId, "FlakyTest", 0, false, 204,  Date())
        val resultRetryFlakyTest = Result(runId, "FlakyTest", 1, true, 120,  Date())

        val resultPassingTest = Result(runId, "PassingTest", 0, true, 34, Date())

        val resultFailingTest = Result(runId, "FailingTest", 0, false, 23, Date())
        val resultRetryFailingTest = Result(runId, "FailingTest", 1, false, 30, Date())

        saveResults(listOf(resultFlakyTest, resultRetryFlakyTest, resultPassingTest, resultFailingTest, resultRetryFailingTest))


        given()
                .get("/runs/$runId/results/grouped")
                .then()
                .statusCode(200)
                .body("PASSED[0].testName", equalTo(resultPassingTest.testName))
                .body("RETRIED[0].testName", equalTo(resultFlakyTest.testName))
                .body("FAILED[0].testName", equalTo(resultFailingTest.testName))
    }

    @Test fun getRunDiffReturnsDiffOfRun() {
        val testSuiteId = randomUUID()
        val prevRun = Run(randomUUID(), testSuiteId, "abc-123", Date(1))
        runRepository.save(prevRun)
        val run = Run(randomUUID(), testSuiteId, "def-456", Date(2))
        runRepository.save(run)

        val resultPrev = Result(prevRun.id, "MyTest", 0, false, 120, Date())
        val result = Result(run.id, resultPrev.testName, 0, true, 23, Date())
        saveResults(listOf(resultPrev, result))

        given()
                .get("/runs/${run.id}/results/diff")
                .then()
                .statusCode(200)
                .body("FIXED[0].testName", equalTo(result.testName))
    }

    @Test fun getRunDiffReturns404IfRunDoesNotExist() {
        val runId = randomUUID()

        given()
                .get("/runs/$runId/results/diff")
                .then()
                .statusCode(404)
    }

    @Test fun getRunDiffReturnsResultsIfNoPreviousRun() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        runRepository.save(run)

        given()
                .get("/runs/${run.id}/results/diff")
                .then()
                .statusCode(200)
    }

    @Test fun getResultByRunAndTestNameReturnsResult() {
        val runId = randomUUID()
        val result = Result(runId, "myTest", 0, true, 42, Date())
        saveResults(listOf(result))

        given()
                .get("/runs/$runId/results/filtered?testname=${result.testName}")
                .then()
                .statusCode(200)
                .body("testName", equalTo(result.testName))
    }

    @Test fun getResultsByRunAndTestNameReturns404IfNoResultCanBeFound() {
        val runId = randomUUID()
        given()
                .get("/runs/$runId/results/filtered?testname=myTest")
                .then()
                .statusCode(404)
    }

    @Test fun getResultsByTestSuiteAndTestNameReturnsResults() {
        val testSutiteId = randomUUID()
        val run = Run(randomUUID(), testSutiteId, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42,  Date())
        saveResults(listOf(result))

        given()
                .get("/testsuites/$testSutiteId/tests/paged/${result.testName!!}")
                .then()
                .statusCode(200)
                .body("results[0].testName", equalTo(result.testName!!))
    }

    @Test fun getResultsByTestSuiteAndTestNameDecodesTestName() {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "ch.yvu.teststore.common.CassandraRepositoryTest#canSaveAnItem", 0, true, 42, Date())
        saveResults(listOf(result))

        given()
                .get("/testsuites/$testSuiteId/tests/paged/ch.yvu.teststore.common.CassandraRepositoryTest%23canSaveAnItem")
                .then()
                .statusCode(200)
                .body("results[0].testName", equalTo(result.testName!!))
    }

    private fun saveResults(results: List<Result>) {
        results.forEach { resultRepository.save(it) }
    }
}