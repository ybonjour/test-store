package ch.yvu.teststore.integration.test

import ch.yvu.teststore.Application
import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.*
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner


@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(Application::class))
class TestControllerTest {

    @Value("\${local.server.port}")
    var port: Int = 0

    @Before fun setUp() {
        RestAssured.port = port
    }

    @Test fun itResponds() {
        get("/tests").then().assertThat().statusCode(200)
    }


    @Test fun itReturnsAStoredTest() {
        val testName = "FooTest"

        given().queryParam("name", testName).post("/tests").then().assertThat().statusCode(200)

        get("/tests").then().assertThat().body("[0].name", equalTo(testName))
    }
}