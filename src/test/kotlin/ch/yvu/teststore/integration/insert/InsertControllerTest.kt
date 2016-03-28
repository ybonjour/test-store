package ch.yvu.teststore.integration.insert

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import com.jayway.restassured.RestAssured
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class InsertControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun insertReturnsCorrectStatusCode() {
        RestAssured.given()
                .post("/insert")
                .then().assertThat().statusCode(200)
    }
}