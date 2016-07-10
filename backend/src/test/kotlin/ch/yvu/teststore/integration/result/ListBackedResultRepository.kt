package ch.yvu.teststore.integration.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import java.util.*

open class ListBackedResultRepository(val genericRepository: ListBackedRepository<Result>) : ResultRepository {
    override fun findAllByTestName(testName: String): List<Result> {
        return genericRepository.findAll { it.testName == testName }
    }

    override fun findAllByRunIdAndTestName(runId: UUID, testName: String): List<Result> {
        return genericRepository.findAll { it.run == runId && it.testName == testName }
    }

    override fun findAllByRunId(runId: UUID): List<Result> {
        return genericRepository.findAll { runId.equals(it.run) }
    }

    override fun save(item: Result): Result {
        return genericRepository.save(item)
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun findAll(): List<Result> {
        return genericRepository.findAll()
    }


    override fun count(): Long {
        return genericRepository.count()
    }
}