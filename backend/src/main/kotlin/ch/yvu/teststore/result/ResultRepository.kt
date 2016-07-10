package ch.yvu.teststore.result

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ResultRepository : TestStoreRepository<Result> {
    fun findAllByRunId(runId: UUID): List<Result>

    fun findAllByRunIdAndTestName(runId: UUID, testName: String): List<Result>

    fun findAllByTestName(testName: String): List<Result>
}

