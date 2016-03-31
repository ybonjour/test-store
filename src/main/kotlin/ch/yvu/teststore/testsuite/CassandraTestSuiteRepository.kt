package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired

open class CassandraTestSuiteRepository @Autowired constructor(session: Session) :
        TestSuiteRepository, CassandraRepository<TestSuite>(MappingManager(session), "testsuite", TestSuite::class.java)