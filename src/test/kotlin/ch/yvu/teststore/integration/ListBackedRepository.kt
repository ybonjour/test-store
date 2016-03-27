package ch.yvu.teststore.integration

import ch.yvu.teststore.common.Model
import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.data.cassandra.repository.MapId

class ListBackedRepository<M : Model> : CassandraRepository<M> {
    var entries = emptyList<M>()


    override fun count(): Long {
        return entries.count().toLong()
    }

    override fun <S : M> save(entities: MutableIterable<S>?): MutableIterable<S>? {
        throw UnsupportedOperationException()
    }

    override fun <S : M> save(entity: S): S {
        entries += entity
        return entity
    }

    override fun exists(id: MapId?): Boolean {
        throw UnsupportedOperationException()
    }

    override fun delete(entity: M) {
        throw UnsupportedOperationException()
    }

    override fun delete(entities: MutableIterable<M>?) {
        throw UnsupportedOperationException()
    }

    override fun delete(id: MapId?) {
        throw UnsupportedOperationException()
    }

    override fun findAll(): MutableIterable<M> {
        return entries.toMutableList()
    }

    override fun findAll(ids: MutableIterable<MapId>?): MutableIterable<M>? {
        throw UnsupportedOperationException()
    }

    override fun findOne(id: MapId?): M {
        throw UnsupportedOperationException()
    }

    override fun deleteAll() {
        entries = emptyList()
    }
}