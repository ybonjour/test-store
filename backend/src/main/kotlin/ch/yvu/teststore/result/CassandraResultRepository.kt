package ch.yvu.teststore.result

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired

open class CassandraResultRepository @Autowired constructor(session: Session) :
        ResultRepository, CassandraRepository<Result>(MappingManager(session), "result", Result::class.java)