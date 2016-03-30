package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired

open class CassandraTestSuiteRepository @Autowired constructor(session: Session) :
        TestSuiteRepository, CassandraRepository<TestSuite>(session, "testsuite", TestSuite::class.java)