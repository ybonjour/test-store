package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import java.util.*

open class ListBackedTestSuiteRepository(val genericRepository: ListBackedRepository<TestSuite>) : TestSuiteRepository {
    override fun findById(id: UUID): TestSuite? {
        return genericRepository.findAll { it.id == id }.firstOrNull()
    }

    override fun findAll(): List<TestSuite> {
        return genericRepository.findAll().toList()
    }

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun save(item: TestSuite): TestSuite {
        return genericRepository.save(item)
    }

    override fun count(): Long {
        return genericRepository.count()
    }
}