package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.matchers.ResultMatchers.resultWith
import ch.yvu.teststore.matchers.RunMatchers.runWith
import ch.yvu.teststore.matchers.RunMatchers.runWithId
import ch.yvu.teststore.matchers.TestMatchers.testWithName
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.test.TestRepository
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
        val TEST_SUITE_DTO = TestSuiteDto("My Test Suite")
        val EMPTY_RUN_DTO = RunDto(revision="abc123", results = emptyList())
        val A_TEST_DTO = TestDto("ATest")
        val B_TEST_DTO = TestDto("BTest")
        val A_TEST_DTO_PASSED = ResultDto(test = A_TEST_DTO, retryNum = 0, passed = true)
        val B_TEST_DTO_FAILED = ResultDto(test = B_TEST_DTO, retryNum = 0, passed = false)
        val TEST_SUITE_ID = randomUUID()
    }

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository
    lateinit var resultRepository: ResultRepository
    lateinit var testRepository: TestRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = mock(TestSuiteRepository::class.java)
        runRepository = mock(RunRepository::class.java)
        resultRepository = mock(ResultRepository::class.java)
        testRepository = mock(TestRepository::class.java)
        insertService = InsertService(testSuiteRepository, runRepository, resultRepository, testRepository)
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
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED))

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyTestAndResultSaved(A_TEST_DTO_PASSED)
        verifyNoMoreInteractions()

    }

    @Test fun insertRunWithMultipleTestResultsCorrectly() {
        val runDto = RunDto(revision = "abcd123", results = listOf(A_TEST_DTO_PASSED, B_TEST_DTO_FAILED))

        insertService.insertRun(runDto, TEST_SUITE_ID)

        verifyRunSaved(runDto, TEST_SUITE_ID)
        verifyTestAndResultSaved(A_TEST_DTO_PASSED)
        verifyTestAndResultSaved(B_TEST_DTO_FAILED)
        verifyNoMoreInteractions()
    }

    private fun verifyTestAndResultSaved(resultDto: ResultDto) {
        verify(testRepository).save(argThat(testWithName(resultDto.test.name)))
        verify(resultRepository).save(argThat(resultWith(
                runId = any(UUID::class.java),
                testId = any(UUID::class.java),
                retryNum = resultDto.retryNum,
                passed = resultDto.passed
        )))
    }

    private fun verifyRunSaved(runDto: RunDto, testSuiteId: UUID) {
        verify(runRepository).save(argThat(runWith(runDto.revision, testSuiteId)))
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(testSuiteRepository)
        verifyNoMoreInteractions(runRepository)
        verifyNoMoreInteractions(resultRepository)
        verifyNoMoreInteractions(testRepository)
    }
}