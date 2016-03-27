package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Description
import org.hamcrest.Matchers.hasItem
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
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
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .post("/results")
                .then().assertThat().statusCode(201)
    }

    @Test fun createResultStoresResultWithCorrectAttributes() {
        val run = randomUUID()
        val test = randomUUID()
        val retryNum = 0
        val passed = true
        given().queryParam("run", run)
                .queryParam("test", test)
                .queryParam("retryNum", retryNum)
                .queryParam("passed", passed)
                .post("/results")

        val results = resultRepository.findAll()
        assertEquals(1, results.count())
        assertThat(results, hasItem(resultWith(run, test, retryNum, passed)))
    }

    private fun resultWith(run: UUID, test: UUID, retryNum: Int, passed: Boolean) = object : TypeSafeMatcher<Result>() {
        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Result with run ${run}, test ${test}, retryNum ${retryNum}, passed ${passed}")
        }

        override fun matchesSafely(item: Result?): Boolean {
            if (item == null) return false

            return item.run == run && item.test == test && item.retryNum == retryNum && item.passed == passed
        }

    }

}