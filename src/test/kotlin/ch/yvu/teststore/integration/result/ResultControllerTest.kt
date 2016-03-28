package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.ResultRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.*
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
                .queryParam("test", randomUUID())
                .queryParam("testName", "MyTest")
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .post("/results")
                .then().assertThat().statusCode(201)
    }

    @Test fun createResultStoresResultWithCorrectAttributes() {
        val run = randomUUID()
        val test = randomUUID()
        val testName = "MyTest"
        val retryNum = 0
        val passed = true
        given().queryParam("run", run)
                .queryParam("test", test)
                .queryParam("testName", testName)
                .queryParam("retryNum", retryNum)
                .queryParam("passed", passed)
                .post("/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(equalTo(run), testName, retryNum, passed)))
    }

    @Test fun createResultReturnsId() {
        given().queryParam("run", randomUUID())
                .queryParam("test", randomUUID())
                .queryParam("testName", "MyTest")
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .post("/results")
        .then().assertThat().body("id", not(isEmptyOrNullString()))
    }
}