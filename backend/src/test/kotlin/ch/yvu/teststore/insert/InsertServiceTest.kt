package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.integration.statistics.ListBackedTestStatisticsRepository
import ch.yvu.teststore.integration.testsuite.ListBackedTestSuiteRepository
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.statistics.TestStatistics
import ch.yvu.teststore.statistics.TestStatisticsRepository
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.hamcrest.Matchers.any
import org.hamcrest.Matchers.hasItem
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.UUID.randomUUID

class InsertServiceTest {

    companion object {
        val TODAY = Date()
        val TEST_SUITE_DTO = TestSuiteDto("My Test Suite")
        val EMPTY_RUN_DTO = RunDto(revision = "abc123", time = TODAY, results = emptyList())
        val A_TEST_DTO_PASSED = ResultDto(testName = "ATest", retryNum = 0, passed = true, durationMillis = 10, time=TODAY)
        val B_TEST_DTO_FAILED = ResultDto(testName = "BTest", retryNum = 0, passed = false, durationMillis = 10, time=TODAY, stackTrace = "stacktrace")
        val TEST_SUITE_ID = randomUUID()
    }

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository
    lateinit var testStatisticsRepository: TestStatisticsRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = ListBackedTestSuiteRepository(ListBackedRepository())
        runRepository = ListBackedRunRepository(ListBackedRepository())
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        testStatisticsRepository = ListBackedTestStatisticsRepository(ListBackedRepository())
        insertService = InsertService(testSuiteRepository, runRepository, resultRepository, testStatisticsRepository)
    }

    @Test fun insertsTestSuiteCorrectly() {
        insertService.insertTestSuite(TEST_SUITE_DTO)

        val testSuites = testSuiteRepository.findAll()
        assertEquals(1, testSuites.count());
        assertEquals(TEST_SUITE_DTO.name, testSuites[0].name)
    }

    @Test fun returnsCorrectTestSuiteAfterCreation() {
        val testSuite = insertService.insertTestSuite(TEST_SUITE_DTO)

        assertNotNull(testSuite.id)
        assertEquals(TEST_SUITE_DTO.name, testSuite.name)
        assertEquals(testSuiteRepository.findAll()[0].id, testSuite.id)
    }

    @Test fun insertsRunWithOneTestResultCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED), time = TODAY)

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyResultsSaved(A_TEST_DTO_PASSED)
    }

    @Test fun returnsCorrectRunAfterCreation() {
        val run = insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID).get()

        assertNotNull(run.id)
        assertEquals(EMPTY_RUN_DTO.revision, run.revision)
        assertEquals(runRepository.findAll()[0].id, run.id)
    }

    @Test fun insertsEmptyRunCorrectly() {
        insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID)

        verifyRunSaved(EMPTY_RUN_DTO, TEST_SUITE_ID)
    }

    @Test fun insertRunWithMultipleTestResultsCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED), time = TODAY)

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyResultsSaved(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED)
    }

    @Test fun insertOneTestResultCorrectly() {
        val runId = randomUUID()
        val resultDtos = listOf(A_TEST_DTO_PASSED)

        insertService.insertResults(resultDtos, runId)

        verifyResultsSaved(A_TEST_DTO_PASSED)
    }

    @Test fun insertTwoTestResultsCorrectly() {
        val runId = randomUUID()
        val resultDtos = listOf(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED)

        insertService.insertResults(resultDtos, runId)

        verifyResultsSaved(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED)
    }

    @Test fun insertsEmptyListOfResultsCorrectly() {
        val runId = randomUUID()
        val resultDtos = emptyList<ResultDto>()

        insertService.insertResults(resultDtos, runId)

        assertEquals(0, resultRepository.count())
    }

    @Test fun insertsTestStatisticsForNewPassedResultCorrectly() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        runRepository.save(run)

        val resultDtos = listOf(A_TEST_DTO_PASSED)

        insertService.insertResults(resultDtos, run.id!!)

        verifyTestStatisticsStored(listOf(TestStatistics(run.testSuite!!, A_TEST_DTO_PASSED.testName, numPassed = 1, numFailed = 0)))
    }

    @Test fun insertsTestStatisticsForNewFailedResultCorrectly() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        runRepository.save(run)

        val resultDtos = listOf(B_TEST_DTO_FAILED)

        insertService.insertResults(resultDtos, run.id!!)

        verifyTestStatisticsStored(listOf(TestStatistics(run.testSuite!!, B_TEST_DTO_FAILED.testName, numPassed = 0, numFailed = 1)))
    }

    @Test fun updatesExistingTestStatisticsCorrectly() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        runRepository.save(run)
        val testStatistic = TestStatistics(run.testSuite, A_TEST_DTO_PASSED.testName, numPassed = 1, numFailed = 0)
        testStatisticsRepository.save(testStatistic)
        val resultDtos = listOf(A_TEST_DTO_PASSED)

        insertService.insertResults(resultDtos, run.id!!)

        verifyTestStatisticsStored(listOf(TestStatistics(run.testSuite, A_TEST_DTO_PASSED.testName, numPassed = 2, numFailed = 0)))
    }

    @Test fun multipleTestStatisticsAreStoredCorrectly() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        runRepository.save(run)
        val resultDtos = listOf(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED)

        insertService.insertResults(resultDtos, run.id!!)

        verifyTestStatisticsStored(listOf(
                TestStatistics(run.testSuite, A_TEST_DTO_PASSED.testName, numPassed = 1, numFailed = 0),
                TestStatistics(run.testSuite, B_TEST_DTO_FAILED.testName, numPassed = 0, numFailed = 1)))
    }

    private fun verifyTestStatisticsStored(expectedTestStatistics: List<TestStatistics>) {
        val testStatistics = testStatisticsRepository.findAll()
        assertEquals(expectedTestStatistics, testStatistics)
    }

    private fun verifyResultsSaved(vararg resultDtos: ResultDto) {
        val results = resultRepository.findAll()
        assertEquals(resultDtos.size, results.size)

        resultDtos.forEach {
            assertThat(results, hasItem(resultWith(
                    runId = any(UUID::class.java),
                    testName = it.testName,
                    retryNum = it.retryNum,
                    passed = it.passed,
                    durationMillis = it.durationMillis,
                    time = TODAY,
                    stackTrace = it.stackTrace
            )))
        }
    }

    private fun verifyRunSaved(runDto: RunDto, testSuiteId: UUID) {
        val runs = runRepository.findAll()
        assertEquals(1, runs.size)
        val run = runs[0]
        assertEquals(runDto.revision, run.revision)
        assertEquals(runDto.time, run.time)
        assertEquals(testSuiteId, run.testSuite)
    }
}