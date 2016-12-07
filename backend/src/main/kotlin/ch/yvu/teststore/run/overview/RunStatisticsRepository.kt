package ch.yvu.teststore.run.overview

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RunStatisticsRepository : TestStoreRepository<RunStatistics> {
    fun findByRunId(runId: UUID): RunStatistics?
}