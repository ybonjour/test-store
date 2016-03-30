package ch.yvu.teststore.integration

import ch.yvu.teststore.common.Model

class ListBackedRepository<M : Model> {
    var entries = emptyList<M>()


    fun count(): Long {
        return entries.count().toLong()
    }

    fun save(entity: M): M {
        entries += entity
        return entity
    }

    fun findAll(): List<M> {
        return entries
    }

    fun deleteAll() {
        entries = emptyList()
    }
}