package ch.yvu.teststore.integration

import com.jayway.restassured.RestAssured
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Value

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = arrayOf(RepositoryMockingConfiguration::class))
@RunWith(SpringJUnit4ClassRunner::class)
abstract class BaseIntegrationTest {

    @Value("\${local.server.port}")

    var port: Int = 0

    @Before
    open fun setUp() {
        RestAssured.port = port
    }
}

