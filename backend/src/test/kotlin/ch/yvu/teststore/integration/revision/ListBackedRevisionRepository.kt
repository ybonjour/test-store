package ch.yvu.teststore.integration.revision

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.revision.Revision
import ch.yvu.teststore.revision.RevisionRepository
import java.util.*

open class ListBackedRevisionRepository(val genericRepository: ListBackedRepository<Revision>) : RevisionRepository {
    override fun save(item: Revision): Revision {
        return genericRepository.save(item)
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun findAll(): List<Revision> {
        return genericRepository.findAll()
    }

    override fun count(): Long {
        return genericRepository.count()
    }

    override fun findAllByRunId(runId: UUID): List<Revision> {
        return genericRepository.findAll { it.run == runId }
    }

}