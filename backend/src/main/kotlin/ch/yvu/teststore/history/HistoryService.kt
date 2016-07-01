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

    fun getRunHistories(testSuiteId: UUID, n: Int): List<RunHistory> {
        return runRepository.findAllByTestSuiteId(testSuiteId).take(n).map {
            val results = resultService.getTestsWithResults(it.id!!)
            val simpleResults = mutableMapOf<String, TestWithResults.TestResult>()
            results.forEach { simpleResults.put(it.testName, it.getTestResult()) }
            RunHistory(it.revision!!, simpleResults)
        }
    }
}