package ch.yvu.teststore.history

import ch.yvu.teststore.result.ResultService
import ch.yvu.teststore.result.TestWithResults
import ch.yvu.teststore.run.RunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class HistoryService @Autowired constructor(
        open val runRepository: RunRepository,
        open val resultService: ResultService) {

    fun getAllTestnames(testSuiteId: UUID, numRuns: Int): List<String> {
        return runRepository.findAllByTestSuiteId(testSuiteId, numRuns).fold(emptySet<String>(),
                { testNames, run ->
                    val results = resultService.getTestsWithResults(run.id!!)
                    testNames.plus(results.map { it.testName })
                }).toList()
    }

    fun getResultsForTests(testSuiteId: UUID, testnames: List<String>, numRuns: Int): List<RunHistory> {
        return runRepository.findAllByTestSuiteId(testSuiteId, numRuns).map {
            val results = resultService.getTestsWithResults(it.id!!)
            val simpleResults = mutableMapOf<String, TestWithResults.TestResult>()
            results.forEach {
                simpleResults.put(it.testName, it.getTestResult())
            }

            val testResults = testnames.map {
                simpleResults.getOrElse(it, { TestWithResults.TestResult.UNKNOWN })
            }

            RunHistory(it.revision ?: "Unknown", it.id!!, emptyMap(), testResults)

        }
    }
}