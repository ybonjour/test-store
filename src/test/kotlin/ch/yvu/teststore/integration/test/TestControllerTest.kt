package ch.yvu.teststore.integration.test

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.test.TestRepository
import com.jayway.restassured.RestAssured.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TestControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var testRepository: TestRepository

    @Before override fun setUp() {
        super.setUp()
        testRepository.deleteAll()
    }

    @Test fun createTestReturnsCorrectStatusCode() {
        given().queryParam("name", "ATest").post("/tests").then().assertThat().statusCode(201)
    }

    @org.junit.Test fun itCanStoreATest() {
        val testName = "ATest"

        given().queryParam("name", testName).post("/tests")

        assertEquals(1, testRepository.count())
    }
}