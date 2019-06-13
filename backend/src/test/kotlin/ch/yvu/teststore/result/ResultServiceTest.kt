package ch.yvu.teststore.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.integration.run.runInstance
import ch.yvu.teststore.result.TestWithResults.TestResult
import ch.yvu.teststore.result.TestWithResults.TestResult.PASSED
import ch.yvu.teststore.run.RunRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class ResultServiceTest {
    companion object {
        val runId = randomUUID()
        val passedResult = Result(runId, "myTest", 0, true, 10, Date())
        val failedResult = Result(runId, "myOtherTest", 0, false, 10, Date())
        val retriedResultFirst = Result(runId, "myRetriedTest", 0, false, 10, Date())
        val retriedResultSecond = Result(runId, "myRetriedTest", 1, true, 10, Date())
    }

    lateinit var resultRepository: ResultRepository
    lateinit var runRepository: RunRepository

    lateinit var resultService: ResultService

    @Before fun setUp() {
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        runRepository = ListBackedRunRepository(ListBackedRepository())

        resultService = ResultService(resultRepository, runRepository)
    }

    @Test fun getTestsWithResultsReturnsTestResults() {
        resultRepository.save(passedResult)

        val testResults = resultService.getTestsWithResults(runId)

        assertEquals(listOf(fromResult(passedResult)), testResults)
    }

    @Test fun getTestWithResultsReturnsEmptyListIfNoResults() {
        val testResults = resultService.getTestsWithResults(runId)

        assertEquals(emptyList<TestWithResults>(), testResults)
    }

    @Test fun getTestWithResultsReturnsAllResultsIfNoFilterProvided() {
        resultRepository.save(passedResult)
        resultRepository.save(failedResult)
        resultRepository.save(retriedResultFirst)
        resultRepository.save(retriedResultSecond)

        val testResults = resultService.getTestsWithResults(runId)

        assertEquals(3, testResults.size)
    }

    @Test fun getTestWithResultsReturnsOnlyResultsThatMatchFilter() {
        resultRepository.save(passedResult)
        resultRepository.save(failedResult)

        val testResults = resultService.getTestsWithResults(runId, PASSED)

        assertEquals(listOf(fromResult(passedResult)), testResults)
    }

    @Test fun getTestsWithResultsGroupsResultsByTestName() {
        val passedRetry = Result(failedResult.run, failedResult.testName, 1, true, 25, Date())
        resultRepository.save(failedResult)
        resultRepository.save(passedRetry)

        val testResults = resultService.getTestsWithResults(runId)

        val expectedResult = fromResult(failedResult)
                .addResult(passedRetry)
        assertEquals(listOf(expectedResult), testResults)
    }

    @Test fun getTestsWithResultDoesNotReturnResultsFromOtherRuns() {
        val otherRunId = randomUUID()
        val otherResult = Result(otherRunId, "myOtherTest", 0, true, 24, Date())
        resultRepository.save(otherResult)

        val testResults = resultService.getTestsWithResults(runId)

        assertEquals(emptyList<TestWithResults>(), testResults)
    }

    @Test fun getGroupedResultsGroupsResultsByTestResult() {
        val otherFailedResult = Result(runId, "myOtherTest", 0, false, 24, Date())
        resultRepository.save(passedResult)
        resultRepository.save(otherFailedResult)

        val groupedResults = resultService.getGroupedResults(runId)

        val expected = mapOf(
                Pair(PASSED, listOf(fromResult(passedResult))),
                Pair(TestResult.FAILED, listOf(fromResult(otherFailedResult))))
        assertEquals(expected, groupedResults)
    }

    @Test fun getTestWithResultsFindsResult() {
        resultRepository.save(passedResult)

        val result = resultService.getTestWithResults(runId, passedResult.testName!!)

        assertNotNull(result)
        assertEquals(passedResult.testName, result?.testName)
        assertEquals(listOf(passedResult), result?.results)
    }

    @Test fun getTestWithResultsReturnsNullIfNoResultsAvailable() {
        val result = resultService.getTestWithResults(runId, "Some test")

        assertNull(result)
    }

    @Test fun getTestWithResultsDoesNotFindResultsFromOtherRuns() {
        val otherResult = Result(randomUUID(), "Some other test", 0, true, 23, Date())
        resultRepository.save(otherResult)

        val result = resultService.getTestWithResults(runId, "Some other test")

        assertNull(result)
    }

    @Test fun getTestWithResultsDoesNotFindResultsWithOtherName() {
        val otherResult = Result(runId, "otherTest", 0, true, 24, Date())
        resultRepository.save(otherResult)

        val result = resultService.getTestWithResults(runId, "myTest")

        assertNull(result)
    }

    @Test fun getTestWithResultsGroupsRetries() {
        val retriedResult = Result(runId, failedResult.testName, 1, true, 42, Date())
        resultRepository.save(failedResult)
        resultRepository.save(retriedResult)

        val result = resultService.getTestWithResults(runId, failedResult.testName!!)

        assertEquals(listOf(failedResult, retriedResult), result?.results)
    }

    @Test fun getResultsByTestSuiteAndTestNameReturnsTestsForTestname() {
        val testSuiteId = randomUUID()
        val run = runInstance(testSuite = testSuiteId)
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42, Date())
        resultRepository.save(result)

        val resultPage = resultService.getResultsByTestSuiteAndTestName(testSuiteId, passedResult.testName!!, null)

        assertEquals(passedResult.testName!!, resultPage.results.get(0).testName)
        assertEquals(run.id!!, resultPage.results.get(0).run)
    }

    @Test fun getResultsByTestSuiteAndTestNameDoesNotReturnResultsFromOtherTestSuite() {
        val otherTestSuiteId = randomUUID()
        val run = runInstance(testSuite = otherTestSuiteId)
        runRepository.save(run)
        val result = Result(run.id, "mytest", 0, true, 42, Date())
        resultRepository.save(result)

        val testSuiteId = randomUUID()

        val resultPage = resultService.getResultsByTestSuiteAndTestName(testSuiteId, result.testName!!, null)

        assertEquals(0, resultPage.results.size)
    }

    @Test fun getResultsByTestSuiteAndTestNameDoesNotReturnResultsFromOtherTestName() {
        val testSuiteId = randomUUID()
        val run = runInstance()
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42, Date())
        resultRepository.save(result)

        val resultPage = resultService.getResultsByTestSuiteAndTestName(testSuiteId, "myOtherTest", null)

        assertEquals(0, resultPage.results.size)
    }

    @Test fun getResultsByTestSuiteAndTestNameSkipsEmptyRunsAtTheBeginning() {
        val testSuiteId = randomUUID()
        val emptyRun = runInstance(testSuite = testSuiteId)
        runRepository.save(emptyRun)
        val run = runInstance(testSuite = testSuiteId)
        runRepository.save(run)

        val result = Result(run.id, "myTest", 0, true, 42, Date())
        resultRepository.save(result)

        val resultPage = resultService.getResultsByTestSuiteAndTestName(testSuiteId, result.testName!!, null, 1)

        assertEquals(1, resultPage.results.size)
    }

    @Test fun getResultsByTestSuiteAndTestNameRespectsFetchSize() {
        val testSuiteId = randomUUID()
        val run1 = runInstance(testSuite = testSuiteId)
        runRepository.save(run1)
        val result1 = Result(run1.id, "myTest", 0, true, 42, Date())
        resultRepository.save(result1)

        val run2 = runInstance(testSuite = testSuiteId)
        runRepository.save(run2)
        val result2 = Result(run2.id, result1.testName, 0, true, 42, Date())
        resultRepository.save(result2)

        val resultPage = resultService.getResultsByTestSuiteAndTestName(testSuiteId, result1.testName!!, null, 1)

        assertEquals(result2, resultPage.results.get(0))
    }

    private fun fromResult(result: Result): TestWithResults {
        return TestWithResults(result.testName!!).addResult(result)
    }
}