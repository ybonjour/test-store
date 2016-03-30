package ch.yvu.teststore.common

import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import javax.annotation.PostConstruct

abstract class CassandraRepository<M : Model>(val session: Session, val table: String, val modelClass: Class<M>) {

    private lateinit var mapper: Mapper<M>

    @PostConstruct fun postConstruct() {
        this.mapper = MappingManager(session).mapper(modelClass);
    }

    open fun save(item: M): M {
        mapper.save(item)
        return item
    }

    open fun deleteAll() {
        mapper.deleteQuery("DELETE FROM $table")
    }

    open fun findAll(): List<M> {
        val results = session.execute("SELECT * FROM $table")
        return mapper.map(results).toList()
    }

    open fun count(): Long {
        throw UnsupportedOperationException("not yet implemented")
    }
}