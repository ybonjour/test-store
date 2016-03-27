package ch.yvu.teststore.integration.test

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.test.TestRepository
import com.jayway.restassured.RestAssured.given
import org.junit.Assert.assertEquals
import org.springframework.beans.factory.annotation.Autowired

class TestControllerTest : BaseIntegrationTest() {

    @Autowired
    lateinit var tesRepository: TestRepository

    @org.junit.Test fun itCanStoreATest() {
        val testName = "FooTest"

        given().queryParam("name", testName).post("/tests").then().assertThat().statusCode(200)

        assertEquals(1, tesRepository.count())
    }
}