package ch.yvu.teststore.testsuite

import ch.yvu.teststore.common.TestStoreRepository
import org.springframework.stereotype.Repository

@Repository
interface TestSuiteRepository : TestStoreRepository<TestSuite>
