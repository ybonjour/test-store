package ch.yvu.teststore.result

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface ResultRepository : CassandraRepository<Result> {
}

