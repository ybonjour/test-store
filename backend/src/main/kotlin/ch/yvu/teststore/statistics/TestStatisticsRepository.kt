package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TestStatisticsRepository : TestStoreRepository<TestStatistics> {

    fun findByTestSuiteAndTestName(testSuiteId: UUID, testName: String): TestStatistics?

    fun findAllByTestSuite(testSuiteId: UUID): List<TestStatistics>
}