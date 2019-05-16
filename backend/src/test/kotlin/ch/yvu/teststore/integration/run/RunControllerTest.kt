package ch.yvu.teststore.integration.run

import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.integration.result.ResultControllerTest
import ch.yvu.teststore.matchers.RunMatchers.runWith
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class RunControllerTest : BaseIntegrationTest() {
    companion object {
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val nowString = SimpleDateFormat(isoFormat).format(Date())
        val now = SimpleDateFormat(isoFormat).parse(nowString)
        val testSuite = randomUUID()
    }

    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        runRepository.deleteAll()
        resultRepository.deleteAll()
    }

    @Test fun createRunReturnsCorrectStatusCode() {
        val run = RunDto("abc123", Date(1))
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(ResultControllerTest.isoFormat)
        val json = mapper.writeValueAsString(run)

        given().contentType(JSON)
                .body(json)
                .post("/testsuites/$testSuite/runs")
                .then().assertThat().statusCode(201)
    }

    @Test fun storesRunWithCorrectRevisionAndTestSuite() {
        val run = RunDto("abc123", Date(1))
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(ResultControllerTest.isoFormat)
        val json = mapper.writeValueAsString(run)

        given().contentType(JSON)
                .body(json)
                .post("/testsuites/$testSuite/runs")

        val runs = runRepository.findAll()
        assertEquals(1, runs.count())
        assertThat(runs, hasItem(runWith(revision = run.revision, testSuite = testSuite, time = run.time)))
    }

    @Test fun runIdIsReturnedWhenCreatingARun() {
        val run = RunDto("abc123", Date(1))
        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(ResultControllerTest.isoFormat)
        val json = mapper.writeValueAsString(run)

        given().contentType(JSON)
                .body(json)
                .post("/testsuites/$testSuite/runs")
                .then().assertThat().body("id", not(isEmptyOrNullString()))
    }

    @Test fun getLastRunReturnsRunOverview() {
        val run = Run(randomUUID(), testSuite, "abc123", now)
        val result = Result(run.id, "myTest", 0, true, 20, Date())
        runRepository.save(run)
        resultRepository.save(result)

        given()
                .get("/testsuites/$testSuite/runs/last")
        .then()
            .statusCode(200)
            .body("run.revision", equalTo(run.revision))
            .body("runStatistics.result", equalTo("PASSED"))
            .body("runStatistics.totalDurationMillis", equalTo(20))
    }

    @Test fun getLastRunReturns404IfTherIsNoRunForTheTestSuite() {
        given()
                .get("/testsuites/$testSuite/runs/last")
        .then()
            .statusCode(404)
    }

    @Test fun getRunOverviewsReturnsRunOverviews() {
        val run = Run(randomUUID(), testSuite, "abc123", now)
        val result = Result(run.id, "myTest", 0, true, 20, Date())
        runRepository.save(run)
        resultRepository.save(result)

        given()
            .get("/testsuites/$testSuite/runs/overview")
        .then()
            .statusCode(200)
            .body("results[0].run.revision", equalTo(run.revision))
            .body("results[0].runStatistics.result", equalTo("PASSED"))
            .body("results[0].runStatistics.totalDurationMillis", equalTo(20))
    }
}



