package ch.yvu.teststore.integration.history

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType.JSON
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class HistoryControllerTest : BaseIntegrationTest() {
    companion object {
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    }

    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun getTestNames() {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date(1))
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42, Date(1))
        resultRepository.save(result)

        given()
            .get("/testsuites/$testSuite/history/testnames?limit=1")
        .then()
            .statusCode(200)
            .body("[0]", equalTo(result.testName))
    }

    @Test fun getTestnamesReturns400IfLimitIsNotProvided() {
        given().get("/testsuites/${randomUUID()}/history/testnames").then().statusCode(400)
    }

    @Test fun getResultsReturnsResults() {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date(1))
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42, Date(1))
        resultRepository.save(result)


        val mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(HistoryControllerTest.isoFormat)
        val json = mapper.writeValueAsString(listOf(result.testName))

        given().contentType(JSON).body(json)
            .post("/testsuites/$testSuite/history/results")
        .then()
                .statusCode(200)
                .body("results[0].runId", equalTo(run.id.toString()))
                .body("results[0].results2[0]", equalTo("PASSED"))
    }
}

