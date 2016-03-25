package ch.yvu.teststore.test

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface TestRepository : CassandraRepository<Test>