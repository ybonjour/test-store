package ch.yvu.teststore.integration.history

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.UUID.randomUUID

class HistoryControllerTest : BaseIntegrationTest() {
    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun getHistoryReturnsHistoryWithLimit() {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date())
        runRepository.save(run)
        val otherRun = Run(randomUUID(), testSuite, "abc-123", Date())
        runRepository.save(otherRun)

        given()
                .get("/testsuites/$testSuite/history?limit=1")
        .then()
                .statusCode(200)
                .body(".", hasSize<Collection<String>>(1))
    }


    @Test fun getHistoryReturnsTestNamesAnResults() {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date(1))
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42, Date(1))
        resultRepository.save(result)

        given()
            .get("/testsuites/$testSuite/history-new?limit=1")
        .then()
            .statusCode(200)
            .body("testnames[0]", equalTo("myTest"))
            .body("runHistory[0].results2[0]", equalTo("PASSED"))
    }

    @Test fun getHistoryReturns400IfLimitIsNotProvided() {
        given().get("/testsuites/${randomUUID()}/history-new").then().statusCode(400)
    }
}

