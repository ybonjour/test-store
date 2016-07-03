package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraRunRepository @Autowired constructor(mappingManager: MappingManager) :
        RunRepository, CassandraRepository<Run>(mappingManager, "run", Run::class.java) {

    override fun findLastRunBefore(testSuiteId: UUID, time: Date): Optional<Run> {
        val resultSet = session.execute("select * from run where testsuite=? and time < ? LIMIT 1", testSuiteId, time)
        val result = mapper.map(resultSet)
        if(result.isExhausted) return Optional.empty()
        else return Optional.of(result.one())
    }

    override fun findAllByTestSuiteId(testSuiteId: UUID): List<Run> {
        val resultSet = session.execute("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        return mapper.map(resultSet).all().toList()
    }
}

