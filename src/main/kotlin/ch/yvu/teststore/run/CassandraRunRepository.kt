package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired

open class CassandraRunRepository @Autowired constructor(session: Session) :
        RunRepository, CassandraRepository<Run>(MappingManager(session), "run", Run::class.java)

