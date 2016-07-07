package ch.yvu.teststore.integration.statistics

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import java.util.*

open class ListBackedTestStatisticsRepository(val genericRepository: ListBackedRepository<TestStatistics>) : TestStatisticsRepository {
    override fun findAllByTestSuite(testSuiteId: UUID): List<TestStatistics> {
        return genericRepository.findAll() { it.testSuite == testSuiteId }
    }

    override fun findByTestSuiteAndTestName(testSuiteId: UUID, testName: String): TestStatistics? {
        return findAllByTestSuite(testSuiteId).filter { it.testName == testName }.firstOrNull()
    }

    override fun save(item: TestStatistics): TestStatistics {
        val statistics = findByTestSuiteAndTestName(item.testSuite!!, item.testName!!)
        if (statistics != null) {
            statistics.numPassed = item.numPassed
            statistics.numFailed = item.numFailed
            return statistics
        } else {
            return genericRepository.save(item)
        }
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun findAll(): List<TestStatistics> {
        return genericRepository.findAll()
    }

    override fun count(): Long {
        return genericRepository.count()
    }
}