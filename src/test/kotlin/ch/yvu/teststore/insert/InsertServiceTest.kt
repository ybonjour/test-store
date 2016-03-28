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

    lateinit var testSuiteRepository: TestSuiteRepository
    lateinit var runRepository: RunRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = mock(TestSuiteRepository::class.java)
        runRepository = mock(RunRepository::class.java)
        insertService = InsertService(testSuiteRepository, runRepository)
    }

    @Test fun insertsTestSuiteCorrectly() {
        val testSuite = TestSuiteDto("My Test Suite")
        insertService.insertTestSuite(testSuite)

        verify(testSuiteRepository).save(argThat(testSuiteWithName(testSuite.name)))
        verifyNoMoreInteractions()
    }

    @Test fun returnsCorrectTestSuiteAfterCreation() {
        val testSuiteDto = TestSuiteDto("MyTestSuite")

        val result = insertService.insertTestSuite(testSuiteDto)

        assertNotNull(result.id)
        assertEquals(testSuiteDto.name, result.name)
        verify(testSuiteRepository).save(argThat(testSuiteWithId(result.id)))
    }

    @Test fun insertsEmptyRunCorrectly() {
        val emptyRun = RunDto(revision = "abcd123", results = emptyList())
        val testSuiteId = randomUUID();

        insertService.insertRun(emptyRun, testSuiteId)

        verify(runRepository).save(argThat(runWith(emptyRun.revision, testSuiteId)))
        verifyNoMoreInteractions()
    }

    @Test fun returnsCorrectRunAfterCreation() {
        val run = RunDto(revision = "abcd123", results = emptyList())
        val testSuiteId = randomUUID()

        val result = insertService.insertRun(run, testSuiteId)

        assertNotNull(result.id)
        assertEquals(run.revision, result.revision)
        verify(runRepository).save(argThat(runWithId(result.id)))
    }

    private fun verifyNoMoreInteractions() {
        verifyNoMoreInteractions(testSuiteRepository)
        verifyNoMoreInteractions(runRepository)
    }
}