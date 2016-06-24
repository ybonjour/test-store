package ch.yvu.teststore.integration.history

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers
import org.hamcrest.Matchers.equalTo
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


    @Test fun getHistoryReturnsHistory() {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 3)
        resultRepository.save(result)

        given()
            .get("/testsuites/$testSuite/history")
        .then()
            .statusCode(200).body("[0].revision", equalTo(run.revision))
    }
}

