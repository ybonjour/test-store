package ch.yvu.teststore.run

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RunRepository : TestStoreRepository<Run> {
    fun findLastRunBefore(testSuiteId: UUID, time: Date): Run?
    fun findById(id: UUID): Run?
    fun findAllByTestSuiteId(testSuiteId: UUID, page: String? = null, fetchSize: Int? = null): Page<Run>
    fun findAllByTestSuiteId(testSuiteId: UUID, maxRows: Int): List<Run>
}