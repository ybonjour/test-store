package ch.yvu.teststore.run

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RunRepository : TestStoreRepository<Run> {
    fun findAllByTestSuiteId(testSuiteId: UUID): List<Run>
    fun findLastRunBefore(testSuiteId: UUID, time: Date): Optional<Run>
    fun findById(id: UUID): Run?
}