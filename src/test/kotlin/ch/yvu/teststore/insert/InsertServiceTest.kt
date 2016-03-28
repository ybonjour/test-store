package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.matchers.RunMatchers.runWith
import ch.yvu.teststore.matchers.RunMatchers.runWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.util.UUID.randomUUID

class InsertServiceTest {

    companion object {
        val TEST_SUITE_DTO = TestSuiteDto("My Test Suite")
        val EMPTY_RUN_DTO = RunDto(revision="abc123", results = emptyList())
        val TEST_SUITE_ID = randomUUID()
    }

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = mock(TestSuiteRepository::class.java)
        runRepository = mock(RunRepository::class.java)
        insertService = InsertService(testSuiteRepository, runRepository)
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

        verify(runRepository).save(argThat(runWith(EMPTY_RUN_DTO.revision, TEST_SUITE_ID)))
        verifyNoMoreInteractions()
    }

    @Test fun returnsCorrectRunAfterCreation() {
        val result = insertService.insertRun(EMPTY_RUN_DTO, TEST_SUITE_ID)

        assertNotNull(result.id)
        assertEquals(EMPTY_RUN_DTO.revision, result.revision)
        verify(runRepository).save(argThat(runWithId(result.id)))
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(testSuiteRepository)
        verifyNoMoreInteractions(runRepository)
    }
}