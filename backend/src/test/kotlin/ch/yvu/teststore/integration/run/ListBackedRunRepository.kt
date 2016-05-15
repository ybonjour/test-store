package ch.yvu.teststore.integration.run

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import java.util.*

open class ListBackedRunRepository(val genericRepository: ListBackedRepository<Run>) : RunRepository {
    override fun findAllByTestSuiteId(testSuiteId: UUID): List<Run> {
        return sorted(genericRepository.findAll { run -> testSuiteId == run.testSuite })
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun save(item: Run): Run {
        return genericRepository.save(item)
    }

    override fun findAll(): List<Run> {
        return sorted(genericRepository.findAll())
    }

    override fun count(): Long {
        return genericRepository.count()
    }

    private fun sorted(results: List<Run>): List<Run> {
        return results.sortedByDescending { it.time }
    }
}