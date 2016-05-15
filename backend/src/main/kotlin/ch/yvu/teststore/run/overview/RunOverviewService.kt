package ch.yvu.teststore.run.overview

import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.run.overview.RunOverview.RunResult.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class RunOverviewService @Autowired constructor(
        open val runRepository: RunRepository,
        open val resultRepository: ResultRepository) {

    fun getLastRunOverview(testSuiteId: UUID): Optional<RunOverview> {
        val run = runRepository.findAllByTestSuiteId(testSuiteId).firstOrNull()
        if (run == null) return Optional.empty()

        val results = resultRepository.findAllByRunId(run.id!!)
        val runResult = extractRunResult(results);

        return Optional.of(RunOverview(run, runResult))
    }

    private fun extractRunResult(results: List<Result>): RunOverview.RunResult {
        if (results.isEmpty()) return RunOverview.RunResult.UNKNOWN

        val testsWithResults = results.groupBy { it.testName }
        var runResult = PASSED
        for((testname, testResults) in testsWithResults) {
            val testRunResult = getRunResultForTest(testResults)
            if(testRunResult.isMoreSevere(runResult)) {
                runResult = testRunResult
            }
        }

        return runResult
    }

    private fun getRunResultForTest(results: List<Result>): RunOverview.RunResult {
        val lastResult = results.sortedBy { it.retryNum }.last()
        if(!lastResult.passed!!) return FAILED

        return if(lastResult.isRetry()) PASSED_WITH_RETRIES else PASSED
    }

}