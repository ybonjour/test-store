package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.UUID.randomUUID

@Service
class InsertService @Autowired constructor(
        val testSuiteRepository: TestSuiteRepository,
        val runRepository: RunRepository,
        val resultRepository: ResultRepository) {

    fun insertTestSuite(testSuiteDto: TestSuiteDto): TestSuite {
        val testSuite = TestSuite(randomUUID(), testSuiteDto.name)
        testSuiteRepository.save(testSuite)
        return testSuite
    }

    fun insertRun(runDto: RunDto, testSuiteId: UUID):Run{
        val run = Run(randomUUID(), testSuiteId, runDto.revision, runDto.time)
        runDto.results.forEach {
            val result = Result(randomUUID(), run.id, it.testName, it.retryNum, it.passed)
            resultRepository.save(result)
        }

        runRepository.save(run)
        return run
    }
}