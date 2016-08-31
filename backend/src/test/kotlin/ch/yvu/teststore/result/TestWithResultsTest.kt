package ch.yvu.teststore.result

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class TestWithResultsTest {
    companion object {
        val runId = randomUUID()
        val passedResultTest1 = Result(runId, "test1", 0, true, 20, Date());
        val failedResultTest1 = Result(runId, "test1", 0, false, 20, Date());
    }

    @Test fun onePassedResultIsPassed() {
        val testWithResults = TestWithResults(passedResultTest1.testName!!)
                .addResult(passedResultTest1)

        val testResult = testWithResults.getTestResult()

        assertEquals(TestWithResults.TestResult.PASSED, testResult)
    }

    @Test fun oneFailedResultIsFailed() {
        val testWithResults = TestWithResults(failedResultTest1.testName!!)
                .addResult(failedResultTest1)

        val testResult = testWithResults.getTestResult()

        assertEquals(TestWithResults.TestResult.FAILED, testResult)
    }

    @Test fun noResultsIsPassed() {
        val testWithResults = TestWithResults("myTest")

        val testResult = testWithResults.getTestResult()

        assertEquals(TestWithResults.TestResult.PASSED, testResult)
    }

    @Test fun retryPassedIsRetried() {
        val passedRetry = Result(failedResultTest1.run, failedResultTest1.testName, 1, true, 23, Date());
        val testWithResults = TestWithResults(failedResultTest1.testName!!)
                .addResult(failedResultTest1).addResult(passedRetry)

        val testResult = testWithResults.getTestResult()

        assertEquals(TestWithResults.TestResult.RETRIED, testResult)
    }

    @Test fun retryFailedIsFailed() {
        val failedRetry = Result(failedResultTest1.run, failedResultTest1.testName, 1, false, 23, Date());
        val testWithResults = TestWithResults(failedResultTest1.testName!!)
                .addResult(failedResultTest1).addResult(failedRetry)

        val testResult = testWithResults.getTestResult()

        assertEquals(TestWithResults.TestResult.FAILED, testResult)
    }
}