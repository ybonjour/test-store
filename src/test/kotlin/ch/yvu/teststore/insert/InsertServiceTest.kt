package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class InsertServiceTest {

    lateinit var testSuiteRepository: TestSuiteRepository

    lateinit var insertService: InsertService

    @Before fun setUp() {
        testSuiteRepository = mock(TestSuiteRepository::class.java)
        insertService = InsertService(testSuiteRepository)
    }

    @Test fun insertsEmptyTestSuiteCorrectly() {
        val emptyTestSuite = TestSuiteDto("My Test Suite", emptyList())
        insertService.insertTestSuite(emptyTestSuite)

        verify(testSuiteRepository).save(argThat(testSuiteWithName(emptyTestSuite.name)))
        verifyNoMoreInteractions(testSuiteRepository)
    }
}