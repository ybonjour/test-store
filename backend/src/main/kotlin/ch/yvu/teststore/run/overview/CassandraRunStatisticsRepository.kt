package ch.yvu.teststore.run.overview

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class CassandraRunStatisticsRepository @Autowired constructor(mappingManager: MappingManager) :
        RunStatisticsRepository, CassandraRepository<RunStatistics>(mappingManager, "run_statistics", RunStatistics::class.java) {

    override fun findByRunId(runId: UUID): RunStatistics? {
        val resultSet = session.execute("SELECT * FROM run_statistics WHERE run=?", runId)
        if (resultSet.isExhausted) return null
        return mapper.map(resultSet).one()
    }
}