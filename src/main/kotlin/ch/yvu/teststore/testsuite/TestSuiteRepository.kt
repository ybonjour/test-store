package ch.yvu.teststore.testsuite

import org.springframework.data.cassandra.repository.CassandraRepository
import org.springframework.stereotype.Repository

@Repository
interface TestSuiteRepository : CassandraRepository<TestSuite>
