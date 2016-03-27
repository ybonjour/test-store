package ch.yvu.teststore.run

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface RunRepository : CassandraRepository<Run>