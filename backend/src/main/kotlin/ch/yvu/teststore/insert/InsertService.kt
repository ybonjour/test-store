package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
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
        open val resultRepository: ResultRepository) {

    open fun insertTestSuite(testSuiteDto: TestSuiteDto): TestSuite {
        val testSuite = TestSuite(randomUUID(), testSuiteDto.name)
        testSuiteRepository.save(testSuite)
        return testSuite
    }

    @Async
    open fun insertRun(runDto: RunDto, testSuiteId: UUID): Future<Run> {
        val run = Run(randomUUID(), testSuiteId, runDto.revision, runDto.time)
        runRepository.save(run)

        insertResults(runDto.results, run.id)

        return AsyncResult(run)
    }

    @Async
    open fun insertResults(resultDtos: List<ResultDto>, runId: UUID) {
        resultDtos.forEach {
            val result = Result(runId, it.testName, it.retryNum, it.passed, it.durationMillis)
            resultRepository.save(result)
        }
    }
}