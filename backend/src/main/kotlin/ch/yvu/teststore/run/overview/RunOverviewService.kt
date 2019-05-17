package ch.yvu.teststore.run.overview

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultDiffService
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
        open val resultRepository: ResultRepository,
        val resultDiffService: ResultDiffService) {

    fun getLastRunOverview(testSuiteId: UUID): Optional<RunOverview> {
        val run = runRepository.findAllByTestSuiteId(testSuiteId, 1).firstOrNull()
        if (run == null) return Optional.empty()

        return Optional.of(getRunOverview(run))
    }

    fun getRunOverviews(testSuiteId: UUID, page:String?, fetchSize: Int?=null): Page<RunOverview> {
        return runRepository.findAllByTestSuiteId(testSuiteId, page, fetchSize).map{getRunOverview(it)}
    }

    private fun getRunOverview(run: Run): RunOverview {
        val results = resultRepository.findAllByRunId(run.id!!)
        val runResult = extractRunResult(results);
        val totalDuration = results.map { it.durationMillis!! }.sum()

        val prevRun = runRepository.findLastRunBefore(run.testSuite!!, run.time!!)
        val diff = resultDiffService.findDiff(prevRun?.id, run.id!!)
        val numberPassed = diff[ResultDiffService.DiffCategory.NEW_PASSED]?.size ?: 0
        val numberFailed = diff[ResultDiffService.DiffCategory.NEW_FAILED]?.size ?: 0

        return RunOverview(run, RunStatistics(run.id, runResult, totalDuration, numberPassed, numberFailed, null, null))
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