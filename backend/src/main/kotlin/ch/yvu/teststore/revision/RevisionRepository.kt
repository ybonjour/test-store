package ch.yvu.teststore.revision

import ch.yvu.teststore.common.TestStoreRepository
import java.util.*

interface RevisionRepository: TestStoreRepository<Revision> {
    fun findAllByRunId(runId: UUID): List<Revision>
}