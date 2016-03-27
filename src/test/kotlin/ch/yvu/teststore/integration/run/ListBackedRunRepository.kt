package ch.yvu.teststore.integration.run

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import org.springframework.data.cassandra.repository.MapId

open class ListBackedRunRepository(val genericRepository: ListBackedRepository<Run>) : RunRepository {

    override fun deleteAll() {
        genericRepository.deleteAll()
    }

    override fun <S : Run> save(entities: MutableIterable<S>): MutableIterable<S>? {
        return genericRepository.save(entities)
    }

    override fun <S : Run> save(entity: S): S {
        return genericRepository.save(entity)
    }

    override fun findAll(): MutableIterable<Run>? {
        return genericRepository.findAll()
    }

    override fun findAll(ids: MutableIterable<MapId>): MutableIterable<Run>? {
        return genericRepository.findAll(ids)
    }

    override fun findOne(id: MapId?): Run? {
        return genericRepository.findOne(id)
    }

    override fun delete(entities: MutableIterable<Run>?) {
        return genericRepository.delete(entities)
    }

    override fun delete(entity: Run) {
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