package ch.yvu.teststore.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.result.ResultDiffService.DiffCategory
import ch.yvu.teststore.result.ResultDiffService.DiffCategory.*
import ch.yvu.teststore.run.RunRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID


class ResultDiffServiceTest {
    companion object {
        val prevRun = randomUUID()
        val run = randomUUID()

        val duration:Long = 2

        val resultPassed = Result(run, "MyNewTest", 0, true, duration, Date())
        val resultFailed = Result(run, "MyNewTestFailed", 0, false, duration, Date())
    }

    private lateinit var resultRepository: ResultRepository
    private lateinit var runRepository: RunRepository

    private lateinit var resultDiffService: ResultDiffService

    @Before fun setUp() {
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        runRepository = ListBackedRunRepository(ListBackedRepository())
        val resultService = ResultService(resultRepository, runRepository)
        resultDiffService = ResultDiffService(resultService)
    }

    @Test fun newPassedTestsCategorizedCorrectly() {
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(NEW_PASSED, resultPassed, results)
    }

    @Test fun newFailedTestsCategorizedCorrectly() {
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(NEW_FAILED, resultFailed, results)
    }

    @Test fun newRetriedTestsCategorizedCorrectly() {
        resultRepository.save(resultFailed)
        val resultRetried = Result(run, resultFailed.testName, 1, true, duration, Date())
        resultRepository.save(resultRetried)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(NEW_RETRIED, resultRetried, results)
    }

    @Test fun removedTestsCategorizedCorrectly() {
        val resultPrev = Result(prevRun, "MyOldTest", 0, true, duration, Date())
        resultRepository.save(resultPrev)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(REMOVED, resultPrev, results)
    }

    @Test fun fixedTestsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultPassed.testName, 0, false, duration, Date())
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(FIXED, resultPassed, results);
    }

    @Test fun brokeTestCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultFailed.testName, 0, true, duration, Date())
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(BROKE, resultFailed, results)
    }

    @Test fun brokeTestAfterRetriedIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultFailed.testName, 0, false, duration, Date())
        val resultPrevRetried = Result(prevRun, resultFailed.testName, 1, true, duration, Date())
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPrevRetried)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(BROKE, resultFailed, results)
    }

    @Test fun fixedTestAfterRetriedIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultPassed.testName, 0, false, duration, Date())
        val resultPrevRetried = Result(prevRun, resultPassed.testName, 1, true, duration, Date())
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPrevRetried)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(FIXED, resultPassed, results)
    }

    @Test fun stillFailingTestIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultFailed.testName, 0, false, duration, Date())
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(STILL_FAILING, resultFailed, results)
    }

    @Test fun stillPassingTestIsCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultPassed.testName, 0, true, duration, Date())
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(STILL_PASSING, resultPassed, results)
    }

    @Test fun stillRetriedTestIsCategorizedCorrectly() {
        val testName = "MyRetriedTest"
        val resultPrevFailed = Result(prevRun, testName, 0, false, duration, Date())
        val resultPrevPassed = Result(prevRun, testName, 1, true, duration, Date())
        val resultFailed = Result(run, testName, 0, false, duration, Date())
        val resultPassed = Result(run, testName, 1, true, duration, Date())
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultFailed)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(STILL_RETRIED, resultPassed, results)
    }

    @Test fun nowRetriedAfterPassedTestIsCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultFailed.testName, 0, true, duration, Date())
        val resultRetried = Result(run, resultFailed.testName, 1, true, duration, Date())
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultFailed)
        resultRepository.save(resultRetried)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(RETRIED_AFTER_PASSED, resultRetried, results)
    }

    @Test fun nowRetriedAfterFailedTestIsCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultFailed.testName, 0, false, duration, Date())
        val resultRetried = Result(run, resultFailed.testName, 1, true, duration, Date())
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultFailed)
        resultRepository.save(resultRetried)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(RETRIED_AFTER_FAILED, resultRetried, results)
    }

    @Test fun ifNoPreviousRunIsProvidedResultsAreInNewCategory() {
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(null, run)

        assertResultInCategory(NEW_PASSED, resultPassed, results)
    }


    private fun assertResultInCategory(expectedCategory: DiffCategory, result: Result, actual: Map<DiffCategory, List<TestWithResults>>) {
        assertEquals(1, actual.size)
        assertEquals(1, actual.get(expectedCategory)!!.size)
        assertEquals(result.testName!!, actual.get(expectedCategory)!![0].testName)
    }
}
