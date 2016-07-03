package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TestSuiteRepository : TestStoreRepository<TestSuite> {
    fun findById(id: UUID): TestSuite?
}
