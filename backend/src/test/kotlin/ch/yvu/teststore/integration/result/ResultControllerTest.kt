package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.ResultRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.hamcrest.Matchers.hasItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID

class ResultControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
    }

    @Test fun createResultReturnsCorrectStatusCode() {
        given().queryParam("run", randomUUID())
                .queryParam("testName", "MyTest")
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .queryParam("durationMillis", 10)
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
                .post("/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(equalTo(run), testName, retryNum, passed, durationMillis)))
    }
}