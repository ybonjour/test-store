package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.springframework.data.cassandra.repository.MapId

open class ListBackedTestSuiteRepository(val genericRepository: ListBackedRepository<TestSuite>) : TestSuiteRepository {

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun <S : TestSuite> save(entities: MutableIterable<S>): MutableIterable<S>? {
        return genericRepository.save(entities)
    }

    override fun <S : TestSuite> save(entity: S): S {
        return genericRepository.save(entity)
    }

    override fun findAll(): MutableIterable<TestSuite>? {
        return genericRepository.findAll()
    }

    override fun findAll(ids: MutableIterable<MapId>): MutableIterable<TestSuite>? {
        return genericRepository.findAll(ids)
    }

    override fun findOne(id: MapId?): TestSuite? {
        return genericRepository.findOne(id)
    }

    override fun delete(entities: MutableIterable<TestSuite>?) {
        return genericRepository.delete(entities)
    }

    override fun delete(entity: TestSuite) {
        return genericRepository.delete(entity)
    }

    override fun delete(id: MapId?) {
        genericRepository.delete(id)
    }

    override fun exists(id: MapId?): Boolean {
        return genericRepository.exists(id)
    }

    override fun count(): Long {
        return genericRepository.count()
    }
}