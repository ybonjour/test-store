package ch.yvu.teststore.run.overview

import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
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
        val results = getResults(run.id!!)
        val runResult = extractRunResult(results);
        return Optional.of(RunOverview(run, runResult))
    }

    private fun extractRunResult(results: List<Result>): RunOverview.RunResult {
        if (results.isEmpty()) return RunOverview.RunResult.UNKNOWN

        return if (results.any { it.passed != null && !it.passed!! }) RunOverview.RunResult.FAILED
        else RunOverview.RunResult.PASSED
    }

    fun getResults(runId: UUID): List<Result> {
        return resultRepository.findAllByRunId(runId)
    }
}