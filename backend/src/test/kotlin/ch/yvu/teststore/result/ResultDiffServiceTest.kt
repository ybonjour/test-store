package ch.yvu.teststore.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.result.ResultDiffService.DiffCategory
import ch.yvu.teststore.result.ResultDiffService.DiffCategory.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID.randomUUID


class ResultDiffServiceTest {
    companion object {
        val prevRun = randomUUID()
        val run = randomUUID()

        val duration:Long = 2

        val resultPassed = Result(run, "MyNewTest", 0, true, duration)
        val resultFailed = Result(run, "MyNewTestFailed", 0, false, duration)
    }

    private lateinit var resultDiffService: ResultDiffService
    private lateinit var resultRepository: ResultRepository

    @Before fun setUp() {
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        val resultService = ResultService(resultRepository)
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
        val resultRetried = Result(run, resultFailed.testName, 1, true, duration)
        resultRepository.save(resultRetried)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(NEW_RETRIED, resultRetried, results)
    }

    @Test fun removedTestsCategorizedCorrectly() {
        val resultPrev = Result(prevRun, "MyOldTest", 0, true, duration)
        resultRepository.save(resultPrev)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(REMOVED, resultPrev, results)
    }

    @Test fun fixedTestsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultPassed.testName, 0, false, duration)
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(FIXED, resultPassed, results);
    }

    @Test fun brokeTestCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultFailed.testName, 0, true, duration)
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(BROKE, resultFailed, results)
    }

    @Test fun brokeTestAfterRetriedIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultFailed.testName, 0, false, duration)
        val resultPrevRetried = Result(prevRun, resultFailed.testName, 1, true, duration)
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPrevRetried)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(BROKE, resultFailed, results)
    }

    @Test fun fixedTestAfterRetriedIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultPassed.testName, 0, false, duration)
        val resultPrevRetried = Result(prevRun, resultPassed.testName, 1, true, duration)
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultPrevRetried)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(FIXED, resultPassed, results)
    }

    @Test fun stillFailingTestIsCategorizedCorrectly() {
        val resultPrevFailed = Result(prevRun, resultFailed.testName, 0, false, duration)
        resultRepository.save(resultPrevFailed)
        resultRepository.save(resultFailed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(STILL_FAILING, resultFailed, results)
    }

    @Test fun stillPassingTestIsCategorizedCorrectly() {
        val resultPrevPassed = Result(prevRun, resultPassed.testName, 0, true, duration)
        resultRepository.save(resultPrevPassed)
        resultRepository.save(resultPassed)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(STILL_PASSING, resultPassed, results)
    }

    @Test fun nowRetriedTestIsCategorizedCorrectly() {
        val resultPrevPasseed = Result(prevRun, resultFailed.testName, 0, true, duration)
        val resultRetried = Result(run, resultFailed.testName, 1, true, duration)
        resultRepository.save(resultPrevPasseed)
        resultRepository.save(resultFailed)
        resultRepository.save(resultRetried)

        val results = resultDiffService.findDiff(prevRun, run)

        assertResultInCategory(NOW_FLAKY, resultRetried, results)
    }

    private fun assertResultInCategory(expectedCategory: DiffCategory, result: Result, actual: Map<DiffCategory, List<TestWithResults>>) {
        assertEquals(1, actual.size)
        assertEquals(1, actual.get(expectedCategory)!!.size)
        assertEquals(result.testName!!, actual.get(expectedCategory)!![0].testName)
    }
}
