package ch.yvu.teststore.result

import ch.yvu.teststore.result.ResultDiffService.DiffCategory.*
import ch.yvu.teststore.result.TestWithResults.TestResult.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ResultDiffService @Autowired constructor(
        open val resultService: ResultService) {
    enum class DiffCategory {
        NEW_PASSED, NEW_FAILED, NEW_RETRIED, REMOVED, FIXED, BROKE, NOW_FLAKY, STILL_FAILING, STILL_PASSING
    }


    fun findDiff(prevRunId: UUID?, runId: UUID): Map<DiffCategory, List<TestWithResults>> {
        val prevResults = if (prevRunId == null) emptyList<TestWithResults>() else resultService.getTestsWithResults(prevRunId)

        val prevTestNames = prevResults.map { it.testName }
        val results = resultService.getTestsWithResults(runId)
        val testNames = results.map { it.testName }

        val commonTests = results
                .map { result -> Pair(result, prevResults.find { it.testName == result.testName }) }
                .filter { it.second != null }
                .map { ResultDiff(it.first, it.second!!) }
        val newTests = results.filter { !prevTestNames.contains(it.testName) }
        val removedTests = prevResults.filter { !testNames.contains(it.testName) }

        return mapOf(
                Pair(NEW_PASSED, newTests.filter { it.getTestResult() == PASSED }),
                Pair(NEW_FAILED, newTests.filter { it.getTestResult() == FAILED }),
                Pair(NEW_RETRIED, newTests.filter { it.getTestResult() == RETRIED }),
                Pair(REMOVED, removedTests),
                Pair(FIXED, filterCommonResults(commonTests, { it.isFixed })),
                Pair(BROKE, filterCommonResults(commonTests, { it.isBroken })),
                Pair(STILL_FAILING, filterCommonResults(commonTests, { it.isStillFailing })),
                Pair(STILL_PASSING, filterCommonResults(commonTests, { it.isStillPassing })),
                Pair(NOW_FLAKY, filterCommonResults(commonTests, { it.isNowFlaky })))
                .filter { !it.value.isEmpty() }
    }

    private fun filterCommonResults(commonResults: List<ResultDiff>,
                                    predicate: (ResultDiff) -> Boolean): List<TestWithResults> {
        return commonResults.filter {
            predicate(it)
        }.map { it.result }
    }

    data class ResultDiff(val result: TestWithResults, val prevResult: TestWithResults) {
        val isFixed = prevResult.getTestResult() != PASSED && result.getTestResult() == PASSED

        val isBroken = prevResult.getTestResult() != FAILED && result.getTestResult() == FAILED

        val isStillFailing = prevResult.getTestResult() == FAILED && result.getTestResult() == FAILED

        val isStillPassing = prevResult.getTestResult() == PASSED && result.getTestResult() == PASSED

        val isNowFlaky = prevResult.getTestResult() == PASSED && result.getTestResult() == RETRIED
    }
}