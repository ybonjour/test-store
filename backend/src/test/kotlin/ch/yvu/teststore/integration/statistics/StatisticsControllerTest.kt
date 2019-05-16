package ch.yvu.teststore.integration.statistics

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import io.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.net.URLEncoder
import java.util.UUID.randomUUID


class StatisticsControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var statisticsRepository: TestStatisticsRepository

    @Before override fun setUp() {
        super.setUp()
        statisticsRepository.deleteAll()
    }

    @Test fun getStatisticsByTestSuiteAndTestNameReturnsStatistics() {
        val testSuiteId = randomUUID()
        val statistics = TestStatistics(testSuiteId, "ch.yvu.testsore.MyTest#myTest", 1, 0, 100, 1)
        statisticsRepository.save(statistics)
        val encodedTestName = URLEncoder.encode(statistics.testName, "UTF-8")

        given()
                .get("/testsuites/$testSuiteId/statistics/$encodedTestName")
        .then()
                .statusCode(200)
                .body("testName", equalTo(statistics.testName))
    }

    @Test fun getStatisticsByTestSuiteAndTestNameReturns404IfNoStatisicsIsFound() {
        val testSuiteId = randomUUID()

        given()
            .get("/testsuites/$testSuiteId/statistics/myTest")
        .then()
            .statusCode(404)
    }

    @Test fun getStatisticsByTestSuitePaged() {
        val testSuiteId = randomUUID()

        val statistics = TestStatistics(testSuiteId, "ch.yvu.testsore.MyTest#myTest", 1, 0, 100, 1)
        statisticsRepository.save(statistics)

        given()
            .get("/testsuites/$testSuiteId/statistics")
        .then()
            .statusCode(200)
            .body("results[0].testName", equalTo(statistics.testName))
    }
}