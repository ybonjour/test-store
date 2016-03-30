package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.core.Session
import org.springframework.beans.factory.annotation.Autowired

open class CassandraRunRepository @Autowired constructor(session: Session) :
        RunRepository, CassandraRepository<Run>(session, "run", Run::class.java)

