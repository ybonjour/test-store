package ch.yvu.teststore.integration.statistics

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import com.jayway.restassured.RestAssured.given
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

    @Test fun getStatisticsByTestSuiteReturnsStatistics() {
        val testSuiteId = randomUUID()
        val statistics1 = TestStatistics(testSuiteId, "myTest1", 1, 0, 100)
        val statistics2 = TestStatistics(testSuiteId, "myTest2", 1, 0, 100)
        statisticsRepository.save(statistics1)
        statisticsRepository.save(statistics2)

        given()
                .get("/testsuites/$testSuiteId/statistics")
                .then()
                .statusCode(200)
                .body("[0].testName", equalTo("myTest1"))
                .body("[1].testName", equalTo("myTest2"))
    }

    @Test fun getStatisticsByTestSuiteAndTestNameReturnsStatistics() {
        val testSuiteId = randomUUID()
        val statistics = TestStatistics(testSuiteId, "ch.yvu.testsore.MyTest#myTest", 1, 0, 100)
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
}