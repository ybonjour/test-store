package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.RepositoryMockingConfiguration
import ch.yvu.teststore.testsuite.TestSuiteRepository
import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.given
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(RepositoryMockingConfiguration::class))
class TestSuiteControllerTest() {

    @Autowired lateinit var testSuiteRepository: TestSuiteRepository

    @Value("\${local.server.port}")

    var port: Int = 0

    @Before fun setUp() {
        RestAssured.port = port
    }

    @Test fun canCreateTestSuite() {
        val testSuiteName = "MyTestSuite"

        given().queryParam("name", testSuiteName).`when`().post("/testsuites").then().assertThat().statusCode(201)

        assertEquals(1, testSuiteRepository.count())
    }
}