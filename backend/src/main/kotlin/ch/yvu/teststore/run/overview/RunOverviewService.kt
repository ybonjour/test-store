package ch.yvu.teststore.run.overview

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.run.overview.RunStatistics.RunResult.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class RunOverviewService @Autowired constructor(
        open val runRepository: RunRepository,
        open val resultRepository: ResultRepository) {

    fun getLastRunOverview(testSuiteId: UUID): Optional<RunStatistics> {
        val run = runRepository.findAllByTestSuiteId(testSuiteId, 1).firstOrNull()
        if (run == null) return Optional.empty()

        return Optional.of(getRunOverview(run))
    }

    fun getRunOverviews(testSuiteId: UUID, page:String?, fetchSize: Int?=null): Page<RunStatistics> {
        return runRepository.findAllByTestSuiteId(testSuiteId, page, fetchSize).map{getRunOverview(it)}
    }

    private fun getRunOverview(run: Run): RunStatistics {
        val results = resultRepository.findAllByRunId(run.id!!)
        val runResult = extractRunResult(results);
        val totalDuration = results.map { it.durationMillis!! }.sum()

        return RunStatistics(run, runResult, totalDuration)
    }

    private fun extractRunResult(results: List<Result>): RunStatistics.RunResult {
        if (results.isEmpty()) return RunStatistics.RunResult.UNKNOWN

        val testsWithResults = results.groupBy { it.testName }
        var runResult = PASSED
        for ((testname, testResults) in testsWithResults) {
            val testRunResult = getRunResultForTest(testResults)
            if (testRunResult.isMoreSevere(runResult)) {
                runResult = testRunResult
            }
        }

        return runResult
    }

    private fun getRunResultForTest(results: List<Result>): RunStatistics.RunResult {
        val lastResult = results.sortedBy { it.retryNum }.last()
        if (!lastResult.passed!!) return FAILED

        return if (lastResult.isRetry()) PASSED_WITH_RETRIES else PASSED
    }

}