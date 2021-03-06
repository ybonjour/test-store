package ch.yvu.teststore.result

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.result.TestWithResults.TestResult
import ch.yvu.teststore.run.RunRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
open class ResultService @Autowired constructor(
        open val resultRepository: ResultRepository,
        open val runRepository: RunRepository
) {
    fun getGroupedResults(runId: UUID): Map<TestResult, List<TestWithResults>> {
        return getTestsWithResults(runId).groupBy { it.getTestResult() }
    }

    fun getTestsWithResults(runId: UUID, resultFilter: TestResult? = null): List<TestWithResults> {
        return resultRepository.findAllByRunId(runId)
                .groupBy { it.testName }
                .map {
                    val (name, results) = it
                    TestWithResults(name!!, results)
                }
                .filter {
                    resultFilter == null || it.getTestResult() == resultFilter
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

    fun getResultsByTestSuiteAndTestName(testSuiteId: UUID, testName: String, page :String?=null, fetchSize: Int?=null): Page<Result> {
        val runsPage = runRepository.findAllByTestSuiteId(testSuiteId, page, fetchSize)
        val results = runsPage.results.flatMap {
            resultRepository.findAllByRunIdAndTestName(it.id!!, testName)
        }

        if(runsPage.nextPage == null || !results.isEmpty()) {
            return Page(results, runsPage.nextPage);
        } else {
            return getResultsByTestSuiteAndTestName(testSuiteId, testName, runsPage.nextPage, fetchSize);
        }
    }
}