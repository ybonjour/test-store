package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.integration.ListBackedRepository
import ch.yvu.teststore.integration.result.ListBackedResultRepository
import ch.yvu.teststore.integration.run.ListBackedRunRepository
import ch.yvu.teststore.integration.testsuite.ListBackedTestSuiteRepository
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
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
        val A_TEST_DTO_PASSED = ResultDto(testName = "ATest", retryNum = 0, passed = true, durationSeconds = 10)
        val B_TEST_DTO_FAILED = ResultDto(testName = "BTest", retryNum = 0, passed = false, durationSeconds = 10)
        val TEST_SUITE_ID = randomUUID()
    }

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = ListBackedTestSuiteRepository(ListBackedRepository())
        runRepository = ListBackedRunRepository(ListBackedRepository())
        resultRepository = ListBackedResultRepository(ListBackedRepository())
        insertService = InsertService(testSuiteRepository, runRepository, resultRepository)
    }

    @Test fun insertsTestSuiteCorrectly() {
        insertService.insertTestSuite(TEST_SUITE_DTO)

        val testSuites = testSuiteRepository.findAll()
        assertEquals(1, testSuites.count());
        assertEquals(TEST_SUITE_DTO.name, testSuites[0].name)
    }

    @Test fun returnsCorrectRunAfterCreation() {
        val run = insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID).get()

        assertNotNull(run.id)
        assertEquals(EMPTY_RUN_DTO.revision, run.revision)
        assertEquals(runRepository.findAll()[0].id, run.id)
    }

    @Test fun insertsRunWithOneTestResultCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED), time = TODAY)

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyResultsSaved(A_TEST_DTO_PASSED)
    }

    @Test fun returnsCorrectTestSuiteAfterCreation() {
        val testSuite = insertService.insertTestSuite(TEST_SUITE_DTO)

        assertNotNull(testSuite.id)
        assertEquals(TEST_SUITE_DTO.name, testSuite.name)
        assertEquals(testSuiteRepository.findAll()[0].id, testSuite.id)
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

    private fun verifyResultsSaved(vararg resultDtos: ResultDto) {
        val results = resultRepository.findAll()
        assertEquals(resultDtos.size, results.size)

        resultDtos.forEach {
            assertThat(results, hasItem(resultWith(
                    runId = any(UUID::class.java),
                    testName = it.testName,
                    retryNum = it.retryNum,
                    passed = it.passed,
                    durationSeconds = it.durationSeconds
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