package ch.yvu.teststore.integration.statistics

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID


class StatisticsControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var statisticsRepository: TestStatisticsRepository

    @Before override fun setUp() {
        super.setUp()
        statisticsRepository.deleteAll()
    }

    @Test fun getStatisticsByTestSuiteReturnsStatistics() {
        val testSuiteId = randomUUID()
        val statistics1 = TestStatistics(testSuiteId, "myTest1", 1, 0)
        val statistics2 = TestStatistics(testSuiteId, "myTest2", 1, 0)
        statisticsRepository.save(statistics1)
        statisticsRepository.save(statistics2)

        given()
                .get("/testsuites/$testSuiteId/statistics")
                .then()
                .statusCode(200)
                .body("[0].testName", equalTo("myTest1"))
                .body("[1].testName", equalTo("myTest2"))
    }
}