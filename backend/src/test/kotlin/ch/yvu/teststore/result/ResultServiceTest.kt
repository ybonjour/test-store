package ch.yvu.teststore.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.result.TestWithResults.TestResult
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class ResultServiceTest {
    companion object {
        val runId = randomUUID()
        val passedResult = Result(runId, "myTest", 0, true, 10)
        val failedResult = Result(runId, "myTest", 0, false, 10)
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

    @Test fun getTestsWithResultsGroupsResultsByTestName() {
        val passedRetry = Result(failedResult.run, failedResult.testName, 1, true, 25)
        resultRepository.save(failedResult)
        resultRepository.save(passedRetry)

        val testResults = resultService.getTestsWithResults(runId)

        val expectedResult = fromResult(failedResult)
                .addResult(passedRetry)
        assertEquals(listOf(expectedResult), testResults)
    }

    @Test fun getTestsWithResultDoesNotReturnResultsFromOtherRuns() {
        val otherRunId = randomUUID()
        val otherResult = Result(otherRunId, "myOtherTest", 0, true, 24)
        resultRepository.save(otherResult)

        val testResults = resultService.getTestsWithResults(runId)

        assertEquals(emptyList<TestWithResults>(), testResults)
    }

    @Test fun getGroupedResultsGroupsResultsByTestResult() {
        val otherFailedResult = Result(runId, "myOtherTest", 0, false, 24)
        resultRepository.save(passedResult)
        resultRepository.save(otherFailedResult)

        val groupedResults = resultService.getGroupedResults(runId)

        val expected = mapOf(
                Pair(TestResult.PASSED, listOf(fromResult(passedResult))),
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
        val otherResult = Result(randomUUID(), "Some other test", 0, true, 23)
        resultRepository.save(otherResult)

        val result = resultService.getTestWithResults(runId, "Some other test")

        assertNull(result)
    }

    @Test fun getTestWithResultsDoesNotFindResultsWithOtherName() {
        val otherResult = Result(runId, "otherTest", 0, true, 24)
        resultRepository.save(otherResult)

        val result = resultService.getTestWithResults(runId, "myTest")

        assertNull(result)
    }

    @Test fun getTestWithResultsGroupsRetries() {
        val retriedResult = Result(runId, failedResult.testName, 1, true, 42)
        resultRepository.save(failedResult)
        resultRepository.save(retriedResult)

        val result = resultService.getTestWithResults(runId, failedResult.testName!!)

        assertEquals(listOf(failedResult, retriedResult), result?.results)
    }

    @Test fun getResultsByTestSuiteAndTestNameReturnsTestsForTestname() {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42)
        resultRepository.save(result)

        val results = resultService.getResultsByTestSuiteAndTestName(testSuiteId, passedResult.testName!!)

        assertEquals(passedResult.testName!!, results.get(0).testName)
        assertEquals(run.id!!, results.get(0).run)
    }

    @Test fun getResultsByTestSuiteAndTestNameDoesNotReturnResultsFromOtherTestSuite() {
        val otherTestSuiteId = randomUUID()
        val run = Run(randomUUID(), otherTestSuiteId, "abc-123", Date())
        runRepository.save(run)
        val result = Result(run.id, "mytest", 0, true, 42)
        resultRepository.save(result)

        val testSuiteId = randomUUID()

        val results = resultService.getResultsByTestSuiteAndTestName(testSuiteId, result.testName!!)

        assertEquals(0, results.size)
    }

    @Test fun getResultsByTestSuiteAndTestNameDoesNotReturnResultsFromOtherTestName() {
        val testSuiteId = randomUUID()
        val run = Run(randomUUID(), testSuiteId, "abc-124", Date())
        runRepository.save(run)
        val result = Result(run.id, "myTest", 0, true, 42)
        resultRepository.save(result)

        val results = resultService.getResultsByTestSuiteAndTestName(testSuiteId, "myOtherTest")

        assertEquals(0, results.size)
    }

    private fun fromResult(result: Result): TestWithResults {
        return TestWithResults(result.testName!!).addResult(result)
    }
}