package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.BaseIntegrationTest
import com.jayway.restassured.RestAssured.given
import org.junit.Test
import java.util.UUID.randomUUID

class ResultControllerTest : BaseIntegrationTest() {
    @Test fun createResultReturnsCorrectStatusCode() {
        given().queryParam("run", randomUUID())
                .queryParam("test", randomUUID())
                .queryParam("retryNum", 0)
                .queryParam("passed", true)
                .post("/results")
                .then().assertThat().statusCode(201)
    }

}