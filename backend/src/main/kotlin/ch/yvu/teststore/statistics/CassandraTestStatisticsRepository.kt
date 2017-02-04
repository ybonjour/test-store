package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.CassandraRepository
import ch.yvu.teststore.common.Page
import ch.yvu.teststore.common.QueryFactory
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraTestStatisticsRepository @Autowired constructor(mappingManager: MappingManager, val queryFactory: QueryFactory) :
        TestStatisticsRepository, CassandraRepository<TestStatistics>(mappingManager, "test_statistics", TestStatistics::class.java) {
    override fun findAllByTestSuite(testSuiteId: UUID): List<TestStatistics> {
        val resultSet = session.execute("select * from test_statistics where testsuite=?", testSuiteId)
        return mapper.map(resultSet).all().toList()
    }

    override fun findAllByTestSuitePaged(testSuiteId: UUID, page: String?, fetchSize: Int?): Page<TestStatistics> {
        val query = queryFactory.createQuery("SELECT * FROM test_statistics WHERE testsuite=?", testSuiteId)
        return pagedResultFetcher.fetch(query, page, fetchSize)
    }

    override fun findByTestSuiteAndTestName(testSuiteId: UUID, testName: String): TestStatistics? {
        val resultSet = session.execute("SELECT * from test_statistics WHERE testSuite=? AND testName=?", testSuiteId, testName)
        if(resultSet.isExhausted) return null
        return mapper.map(resultSet).one()
    }

}