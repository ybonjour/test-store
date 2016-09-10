package ch.yvu.teststore.history

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.result.ResultService
import ch.yvu.teststore.result.TestWithResults
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasSize
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class HistoryServiceTest {

    companion object {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc-123", Date())
        val defaultLImit = 25
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

    @Test fun returnsRevisionOfRun() {
        runRepository.save(run)

        val runHistory = historyService.getRunHistories(testSuiteId, defaultLImit)

        assertEquals(1, runHistory.size)
        assertEquals(run.revision, runHistory.get(0).revision)
        assertEquals(run.id, runHistory.get(0).runId)
    }

    @Test fun doesNotReturnRevisionsOfOtherTestSuites() {
        runRepository.save(run)
        val otherTestSuiteId = randomUUID()

        val runHistory = historyService.getRunHistories(otherTestSuiteId, defaultLImit)

        assertTrue(runHistory.isEmpty())
    }

    @Test fun returnsRunsInDescendingOrderByTime() {
        val run1 = Run(randomUUID(), testSuiteId, "abc-123", Date(1))
        runRepository.save(run1)
        val run2 = Run(randomUUID(), testSuiteId, "def-456", Date(2))
        runRepository.save(run2)

        val runHistory = historyService.getRunHistories(testSuiteId, defaultLImit)

        assertEquals(2, runHistory.size)
        assertEquals(run2.revision, runHistory.get(0).revision)
        assertEquals(run1.revision, runHistory.get(1).revision)
    }

    @Test fun historyContainsTestResults() {
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 20, Date(1))
        resultRepository.save(result)

        val runHistory = historyService.getRunHistories(testSuiteId, defaultLImit)

        assertThat(runHistory, hasSize(equalTo(1)));
        assertEquals(runHistory.get(0).results.get(result.testName), TestWithResults.TestResult.PASSED)
    }
}