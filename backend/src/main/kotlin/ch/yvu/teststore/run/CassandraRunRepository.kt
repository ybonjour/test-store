package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraRunRepository @Autowired constructor(mappingManager: MappingManager) :
        RunRepository, CassandraRepository<Run>(mappingManager, "run", Run::class.java) {
    override fun findAllByTestSuiteId(testSuiteId: UUID): List<Run> {
        val resultSet = session.execute("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        return mapper.map(resultSet).all().toList()
    }
}

