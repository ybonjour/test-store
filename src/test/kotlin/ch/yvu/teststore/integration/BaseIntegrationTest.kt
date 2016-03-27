package ch.yvu.teststore.integration

import com.jayway.restassured.RestAssured
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@WebIntegrationTest("server.port=0")
@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(classes = arrayOf(RepositoryMockingConfiguration::class))
abstract class BaseIntegrationTest {

    @Value("\${local.server.port}")

    var port: Int = 0

    @Before open fun setUp() {
        RestAssured.port = port
    }
}

