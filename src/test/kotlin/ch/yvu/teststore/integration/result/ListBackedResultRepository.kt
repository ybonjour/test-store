package ch.yvu.teststore.integration.run

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import org.springframework.data.cassandra.repository.MapId

open class ListBackedResultRepository(val genericRepository: ListBackedRepository<Result>) : ResultRepository {

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun <S : Result> save(entities: MutableIterable<S>): MutableIterable<S>? {
        return genericRepository.save(entities)
    }

    override fun <S : Result> save(entity: S): S {
        return genericRepository.save(entity)
    }

    override fun findAll(): MutableIterable<Result>? {
        return genericRepository.findAll()
    }

    override fun findAll(ids: MutableIterable<MapId>): MutableIterable<Result>? {
        return genericRepository.findAll(ids)
    }

    override fun findOne(id: MapId?): Result? {
        return genericRepository.findOne(id)
    }

    override fun delete(entities: MutableIterable<Result>?) {
        return genericRepository.delete(entities)
    }

    override fun delete(entity: Result) {
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