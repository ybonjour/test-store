package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraTestStatisticsRepository @Autowired constructor(mappingManager: MappingManager) :
        TestStatisticsRepository, CassandraRepository<TestStatistics>(mappingManager, "test_statistics", TestStatistics::class.java) {

    override fun findByTestSuiteAndTestName(testSuiteId: UUID, testName: String): TestStatistics? {
        val resultSet = session.execute("SELECT * from test_statistics WHERE testSuite=? AND testName=?", testSuiteId, testName)
        if(resultSet.isExhausted) return null
        return mapper.map(resultSet).one()
    }

}