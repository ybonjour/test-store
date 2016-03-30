package ch.yvu.teststore.result

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired

open class CassandraResultRepository @Autowired constructor(session: Session) :
        ResultRepository, CassandraRepository<Result>(session, "result", Result::class.java)