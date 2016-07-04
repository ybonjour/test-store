package ch.yvu.teststore.result

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ResultService @Autowired constructor(open val resultRepository: ResultRepository) {
    fun getGroupedResults(runId: UUID): Map<TestWithResults.TestResult, List<TestWithResults>> {
        return getTestsWithResults(runId).groupBy{ it.getTestResult() }
    }

    fun getTestsWithResults(runId: UUID): List<TestWithResults> {
        return resultRepository.findAllByRunId(runId)
                .groupBy { it.testName }
                .map {
                    val (name, results) = it
                    TestWithResults(name!!, results)
                }
    }

    fun getTestWithResults(runId: UUID, testName: String): TestWithResults? {
        return resultRepository.findAllByRunIdAndTestName(runId, testName)
            .groupBy { it.testName }
            .map {
                val (name, results) = it
                TestWithResults(name!!, results)
            }.firstOrNull()
    }
}