package ch.yvu.teststore.history

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.result.ResultService
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class HistoryServiceTest {

    companion object {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc-123", Date())
    }

    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository
    lateinit var resultService: ResultService

    lateinit var historyService: HistoryService

    @Before fun setUp() {
        runRepository = ListBackedRunRepository(ListBackedRepository())
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        resultService = ResultService(resultRepository, runRepository)
        historyService = HistoryService(runRepository, resultService)

    }

    @Test fun getAllTestNamesReturnsTestNamesFromDifferentRuns() {
        val run1 = Run(randomUUID(), testSuiteId, "abc-123", Date(1))
        runRepository.save(run1)
        val result1 = Result(run1.id, "test1", 0, true, 20, Date(1))
        resultRepository.save(result1)

        val run2 = Run(randomUUID(), testSuiteId, "def-456", Date(2))
        runRepository.save(run2)
        val result2 = Result(run2.id, "test2", 0, true, 24, Date(2))
        resultRepository.save(result2)

        val testNames = historyService.getAllTestnames(testSuiteId, numRuns = 2)

        assertThat(testNames, hasSize(equalTo(2)))
        assertThat(testNames, hasItem(result1.testName))
        assertThat(testNames, hasItem(result2.testName))
    }

    @Test fun getAllTestNamesDoesNotContainduplicates() {
        val run1 = Run(randomUUID(), testSuiteId, "abc-123", Date(1))
        runRepository.save(run1)
        val result1 = Result(run1.id, "test", 0, true, 20, Date(1))
        resultRepository.save(result1)

        val run2 = Run(randomUUID(), testSuiteId, "def-456", Date(2))
        runRepository.save(run2)
        val result2 = Result(run2.id, "test", 0, true, 24, Date(2))
        resultRepository.save(result2)

        val testNames = historyService.getAllTestnames(testSuiteId, numRuns = 2)

        assertThat(testNames, hasSize(equalTo(1)))
    }

    @Test fun getAllTestNamesOnlyConsidersFirstNRuns() {
        val run1 = Run(randomUUID(), testSuiteId, "abc-123", Date(1))
        runRepository.save(run1)
        val result1 = Result(run1.id, "test1", 0, true, 20, Date(1))
        resultRepository.save(result1)

        val run2 = Run(randomUUID(), testSuiteId, "def-456", Date(2))
        runRepository.save(run2)
        val result2 = Result(run2.id, "test2", 0, true, 24, Date(2))
        resultRepository.save(result2)

        val testNames = historyService.getAllTestnames(testSuiteId, numRuns = 1)

        assertThat(testNames, hasSize(equalTo(1)))
    }
}