package ch.yvu.teststore.integration.run

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.run.overview.RunStatistics
import ch.yvu.teststore.run.overview.RunStatisticsRepository
import java.util.*

class ListBackedRunStatisticsRepository(val genericRepository: ListBackedRepository<RunStatistics>) : RunStatisticsRepository {
    override fun save(item: RunStatistics): RunStatistics {
        return genericRepository.save(item)
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun findAll(): List<RunStatistics> {
        return genericRepository.findAll()
    }

    override fun count(): Long {
        return genericRepository.count()
    }

    override fun findByRunId(runId: UUID): RunStatistics? {
        return genericRepository.findAll { it.run == runId }.firstOrNull()
    }


}