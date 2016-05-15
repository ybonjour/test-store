package ch.yvu.teststore.run.overview

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class RunOverviewServiceTest {

    companion object {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc123", Date(1))
    }

    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository

    lateinit var runOverviewService: RunOverviewService

    @Before fun setUp() {
        runRepository = ListBackedRunRepository(ListBackedRepository())
        resultRepository = ListBackedResultRepository(ListBackedRepository())

        runOverviewService = RunOverviewService(runRepository, resultRepository)
    }

    @Test fun returnsRunOverview() {
        runRepository.save(run)

        val runOverview = runOverviewService.getLastRunOverview(testSuiteId)

        assertTrue(runOverview.isPresent)
        assertEquals(runOverview.get().run, run)
    }

    @Test fun returnsNoRunOverviewIfThereAreNoRuns() {
        val runOverview = runOverviewService.getLastRunOverview(testSuiteId)

        assertFalse(runOverview.isPresent)
    }

    @Test fun returnsNoRunOverviewIfOnlyRunsFromOtherTestSuiteArePresent() {
        val otherTestSuitedId = randomUUID()
        val otherRun = Run(randomUUID(), otherTestSuitedId, "def123", Date())
        runRepository.save(otherRun)

        val runOverview = runOverviewService.getLastRunOverview(testSuiteId)

        assertFalse(runOverview.isPresent)
    }

    @Test fun returnsLatestRun() {
        val latestRun = Run(randomUUID(), testSuiteId, "abc124", Date(2))
        runRepository.save(run)
        runRepository.save(latestRun)

        val runOverview = runOverviewService.getLastRunOverview(testSuiteId)

        assertEquals(runOverview.get().run, latestRun)
    }
}