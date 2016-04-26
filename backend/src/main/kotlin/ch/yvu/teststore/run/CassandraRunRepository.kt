package ch.yvu.teststore.run

import ch.yvu.teststore.common.CassandraRepository
import com.datastax.driver.mapping.MappingManager
import org.springframework.beans.factory.annotation.Autowired

open class CassandraRunRepository @Autowired constructor(mappingManager: MappingManager) :
        RunRepository, CassandraRepository<Run>(mappingManager, "run", Run::class.java)

