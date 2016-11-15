package ch.yvu.teststore.integration.result

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType.JSON
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
        val runId = randomUUID()
        val result = ResultDto(
                testName="MyTest",
                retryNum = 0,
                passed = true,
                durationMillis = 10,
                time = Date(1)
        )

        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(isoFormat)
        val json = mapper.writeValueAsString(result)

        given()
                .contentType(JSON)
                .body(json)
                .post("/runs/$runId/results")
                .then().assertThat().statusCode(201)
    }

    @Test fun createResultStoresResultWithCorrectAttributes() {
        val runId = randomUUID()
        val result = ResultDto(
                testName="MyTest",
                retryNum = 0,
                passed = true,
                durationMillis = 10,
                time = Date(1)
        )

        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(isoFormat)
        val json = mapper.writeValueAsString(result)

        given()
                .contentType(JSON)
                .body(json)
                .post("/runs/$runId/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(
                equalTo(runId), result.testName, result.retryNum, result.passed, result.durationMillis, result.time, null, null)))
    }

    @Test fun createResultCanCreateFailedResult() {
        val runId = randomUUID()
        val result = ResultDto(
                testName="MyTest",
                retryNum = 0,
                passed = false,
                durationMillis = 10L,
                time = Date(1),
                stackTrace = "stacktrace",
                log = "log"
        )

        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(isoFormat)
        val json = mapper.writeValueAsString(result)

        given()
                .contentType(JSON)
                .body(json)
                .post("/runs/$runId/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(
                equalTo(runId), result.testName, result.retryNum, result.passed, result.durationMillis, result.time, result.stackTrace, result.log)))
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

    @Test fun getResultsByRunAndTestNameDecodesTestName() {
        val runId = randomUUID()
        val result = Result(runId, "package#test", 0, true, 42, Date())
        saveResults(listOf(result))

        given()
                .get("/runs/$runId/results/filtered?testname=package%23test")
                .then()
                .statusCode(200)
                .body("testName", equalTo(result.testName))
    }

    @Test fun getResultsByTestSuiteAndTestNameReturnsResults() {
        val testSutiteId = randomUUID()
        val run = Run(randomUUID(), testSutiteId, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42,  Date())
        saveResults(listOf(result))

        given()
                .get("/testsuites/$testSutiteId/tests/${result.testName!!}")
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
                .get("/testsuites/$testSuiteId/tests/ch.yvu.teststore.common.CassandraRepositoryTest%23canSaveAnItem")
                .then()
                .statusCode(200)
                .body("results[0].testName", equalTo(result.testName!!))
    }

    @Test fun updateFailureReasonReturnsCorrectStatusCode() {
        val result = Result(randomUUID(), "test", 1, false, 20, Date(1), "stacktrace")
        saveResults(listOf(result))
        val failureReason = "Applicatiion Bug"

        saveResults(listOf(result))

        given()
            .queryParam("failureReason", failureReason)
            .put("/runs/${result.run}/tests/${result.testName}/${result.retryNum}")
            .then().statusCode(200)

        assertEquals(failureReason, result.failureReason)
    }


    @Test fun updateFailureReasonDecodesTestNameCorreclty() {
        val result = Result(randomUUID(), "ch.yvu.test#MyTest", 1, false, 20, Date(1), "stacktrace")
        saveResults(listOf(result))
        val failureReason = "Applicatiion Bug"

        saveResults(listOf(result))

        given()
            .queryParam("failureReason", failureReason)
            .put("/runs/${result.run}/tests/ch.yvu.test%23MyTest/${result.retryNum}")
            .then()

        assertEquals(failureReason, result.failureReason)
    }

    private fun saveResults(results: List<Result>) {
        results.forEach { resultRepository.save(it) }
    }
}