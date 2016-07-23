package ch.yvu.teststore.revision

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

class CassandraRevisionRepository @Autowired constructor(mappingManager: MappingManager) : RevisionRepository, CassandraRepository<Revision>(mappingManager, "revision", Revision::class.java) {
    override fun findAllByRunId(runId: UUID): List<Revision> {
        val resultSet = session.execute("SELECT * FROM revision WHERE run=?", runId)
        return mapper.map(resultSet).all().toList()
    }
}