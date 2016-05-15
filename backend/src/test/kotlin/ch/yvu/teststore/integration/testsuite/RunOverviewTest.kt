package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.UUID.randomUUID

class RunOverviewTest : BaseIntegrationTest() {

    companion object {
        val testSuite = TestSuite(randomUUID(), "MyTestSuite");
    }

    @Autowired lateinit var testSuiteRepository: TestSuiteRepository

    @Autowired lateinit var runRepository: RunRepository

    @Before override fun setUp() {
        super.setUp()
        runRepository.deleteAll()
        testSuiteRepository.deleteAll()

        testSuiteRepository.save(testSuite)
    }

    @Test fun findLastRuns() {

    }
}