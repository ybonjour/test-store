package ch.yvu.teststore.revision

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface RevisionRepository: TestStoreRepository<Revision> {
    fun findAllByRunId(runId: UUID): List<Revision>
}