package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.AsyncResult
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID
import java.util.concurrent.Future

@Service
open class InsertService @Autowired constructor(
        open val testSuiteRepository: TestSuiteRepository,
        open val runRepository: RunRepository,
        open val resultRepository: ResultRepository,
        open val testStatisticsRepository: TestStatisticsRepository) {

    open fun insertTestSuite(testSuiteDto: TestSuiteDto): TestSuite {
        val testSuite = TestSuite(randomUUID(), testSuiteDto.name)
        testSuiteRepository.save(testSuite)
        return testSuite
    }

    @Async
    open fun insertRun(runDto: RunDto, testSuiteId: UUID): Future<Run> {
        val runId = randomUUID()
        val run = Run(runId, testSuiteId, runDto.revision, runDto.time)
        runRepository.save(run)

        insertResults(runDto.results, runId)

        return AsyncResult(run)
    }

    @Async
    open fun insertResults(resultDtos: List<ResultDto>, runId: UUID) {
        val run = runRepository.findById(runId)
        resultDtos.forEach {
            val result = Result(runId, it.testName, it.retryNum, it.passed, it.durationMillis, it.time, it.stackTrace)
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

        testStatisticsRepository.save(testStatistics);
    }

    private fun getOrCreateTestStatistics(testSuie: UUID,  testName: String): TestStatistics {
        val statistics = testStatisticsRepository.findByTestSuiteAndTestName(testSuie, testName)
        if(statistics != null) return statistics

        return TestStatistics(testSuie, testName, 0, 0)


    }
}