package ch.yvu.teststore.common

interface TestStoreRepository<M : Model> {
    fun save(item: M): M

    fun deleteAll()

    fun findAll(): List<M>

    fun count(): Long
}