package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

@Service
open class InsertService @Autowired constructor(
        open val runRepository: RunRepository,
        open val resultRepository: ResultRepository,
        open val testStatisticsRepository: TestStatisticsRepository) {

    @Async
    open fun insertResults(resultDtos: List<ResultDto>, runId: UUID) {
        val run = runRepository.findById(runId)
        resultDtos.forEach {
            val result = Result(runId, it.testName, it.retryNum, it.passed, it.durationMillis, it.time, it.stackTrace, it.log)
            resultRepository.save(result)
            if(run != null && run.testSuite != null) {
                storeTestStatistics(run.testSuite!!, result)
            }
        }
    }

    private fun storeTestStatistics(testSuite: UUID, result: Result) {
        val testStatistics = getOrCreateTestStatistics(testSuite, result.testName!!)

        if(result.isPassed()) {
            testStatistics.numPassed = testStatistics.numPassed!! + 1
        } else {
            testStatistics.numFailed = testStatistics.numFailed!! + 1
        }

        testStatistics.durationSum = (testStatistics.durationSum?: 0) + (result.durationMillis?: 0)
        testStatistics.durationNum = testStatistics.durationNum!! + 1

        testStatisticsRepository.save(testStatistics);
    }

    private fun getOrCreateTestStatistics(testSuie: UUID,  testName: String): TestStatistics {
        val statistics = testStatisticsRepository.findByTestSuiteAndTestName(testSuie, testName)
        if(statistics != null) return statistics

        return TestStatistics(testSuie, testName, 0, 0, 0, 0)


    }
}