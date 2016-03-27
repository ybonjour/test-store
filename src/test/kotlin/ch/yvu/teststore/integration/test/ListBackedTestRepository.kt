package ch.yvu.teststore.integration.test

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.test.Test
import ch.yvu.teststore.test.TestRepository
import org.springframework.data.cassandra.repository.MapId

open class ListBackedTestRepository(val genericRepository: ListBackedRepository<Test>) : TestRepository {

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun <S : Test> save(entities: MutableIterable<S>): MutableIterable<S>? {
        return genericRepository.save(entities)
    }

    override fun <S : Test> save(entity: S): S {
        return genericRepository.save(entity)
    }

    override fun findAll(): MutableIterable<Test>? {
        return genericRepository.findAll()
    }

    override fun findAll(ids: MutableIterable<MapId>): MutableIterable<Test>? {
        return genericRepository.findAll(ids)
    }

    override fun findOne(id: MapId?): Test? {
        return genericRepository.findOne(id)
    }

    override fun delete(entities: MutableIterable<Test>?) {
        return genericRepository.delete(entities)
    }

    override fun delete(entity: Test) {
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