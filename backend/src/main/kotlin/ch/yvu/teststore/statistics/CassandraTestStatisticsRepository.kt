package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired

open class CassandraTestStatisticsRepository @Autowired constructor(mappingManager: MappingManager) :
        TestStatisticsRepository, CassandraRepository<TestStatistics>(mappingManager, "test_statistics", TestStatistics::class.java) {

}