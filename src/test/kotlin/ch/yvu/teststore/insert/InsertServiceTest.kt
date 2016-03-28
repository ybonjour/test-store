package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.matchers.RunMatchers.runWith
import ch.yvu.teststore.matchers.RunMatchers.runWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.hamcrest.Matchers.any
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*
import java.util.UUID.randomUUID

class InsertServiceTest {

    companion object {
        val TODAY = Date()
        val TEST_SUITE_DTO = TestSuiteDto("My Test Suite")
        val EMPTY_RUN_DTO = RunDto(revision = "abc123", time = TODAY, results = emptyList())
        val A_TEST_DTO_PASSED = ResultDto(testName = "ATest", retryNum = 0, passed = true)
        val B_TEST_DTO_FAILED = ResultDto(testName = "BTest", retryNum = 0, passed = false)
        val TEST_SUITE_ID = randomUUID()
    }

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = mock(TestSuiteRepository::class.java)
        runRepository = mock(RunRepository::class.java)
        resultRepository = mock(ResultRepository::class.java)
        insertService = InsertService(testSuiteRepository, runRepository, resultRepository)
    }

    @Test fun insertsTestSuiteCorrectly() {
        insertService.insertTestSuite(TEST_SUITE_DTO)

        verify(testSuiteRepository).save(argThat(testSuiteWithName(TEST_SUITE_DTO.name)))
        verifyNoMoreInteractions()
    }

    @Test fun returnsCorrectTestSuiteAfterCreation() {
        val result = insertService.insertTestSuite(TEST_SUITE_DTO)

        assertNotNull(result.id)
        assertEquals(TEST_SUITE_DTO.name, result.name)
        verify(testSuiteRepository).save(argThat(testSuiteWithId(result.id)))
    }

    @Test fun insertsEmptyRunCorrectly() {
        insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID)

        verifyRunSaved(EMPTY_RUN_DTO, TEST_SUITE_ID)
        verifyNoMoreInteractions()
    }

    @Test fun returnsCorrectRunAfterCreation() {
        val run = insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID)

        assertNotNull(run.id)
        assertEquals(EMPTY_RUN_DTO.revision, run.revision)
        verify(runRepository).save(argThat(runWithId(run.id)))
    }

    @Test fun insertsRunWithOneTestResultCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED), time = TODAY)

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyResultSaved(A_TEST_DTO_PASSED)
        verifyNoMoreInteractions()

    }

    @Test fun insertRunWithMultipleTestResultsCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED), time = TODAY)

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyResultSaved(A_TEST_DTO_PASSED)
        verifyResultSaved(B_TEST_DTO_FAILED)
        verifyNoMoreInteractions()
    }

    private fun verifyResultSaved(resultDto: ResultDto) {
        verify(resultRepository).save(argThat(resultWith(
                runId = any(UUID::class.java),
                testName = resultDto.testName,
                retryNum = resultDto.retryNum,
                passed = resultDto.passed
        )))
    }

    private fun verifyRunSaved(runDto: RunDto, testSuiteId: UUID) {
        verify(runRepository).save(argThat(runWith(revision = runDto.revision, time = runDto.time, testSuite = testSuiteId)))
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(testSuiteRepository)
        verifyNoMoreInteractions(runRepository)
        verifyNoMoreInteractions(resultRepository)
    }
}