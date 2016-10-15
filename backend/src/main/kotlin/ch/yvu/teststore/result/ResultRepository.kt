package ch.yvu.teststore.result

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ResultRepository : TestStoreRepository<Result> {
    fun findAllByRunId(runId: UUID): List<Result>

    fun findAllByRunIdAndTestName(runId: UUID, testName: String): List<Result>

    fun updateFailureReason(runId: UUID, testName: String, retryNum: Int, failureReason: String)
}

