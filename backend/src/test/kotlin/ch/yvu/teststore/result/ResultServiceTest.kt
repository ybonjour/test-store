package ch.yvu.teststore.result

import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.result.TestWithResults.TestResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID.randomUUID

class ResultServiceTest {
    companion object {
        val runId = randomUUID()
        val passedResult = Result(runId, "myTest", 0, true, 10)
        val failedResult = Result(runId, "myTest", 0, false, 10)
    }

    lateinit var resultRepository: ResultRepository

    lateinit var resultService: ResultService

    @Before fun setUp() {
        resultRepository = ListBackedResultRepository(ListBackedRepository())

        resultService = ResultService(resultRepository)
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

    private fun fromResult(result: Result): TestWithResults {
        return  TestWithResults(result.testName!!).addResult(result)
    }
}