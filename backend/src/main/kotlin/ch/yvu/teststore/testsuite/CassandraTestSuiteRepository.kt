package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired
import java.util.*

open class CassandraTestSuiteRepository @Autowired constructor(session: Session) :
        TestSuiteRepository, CassandraRepository<TestSuite>(MappingManager(session), "testsuite", TestSuite::class.java) {
    override fun findById(id: UUID): Optional<TestSuite> {
        val resultSet = session.execute("SELECT * FROM testsuite WHERE id=?", id)
        if(resultSet.isExhausted) return Optional.empty()
        return Optional.of(mapper.map(resultSet).one())
    }
}