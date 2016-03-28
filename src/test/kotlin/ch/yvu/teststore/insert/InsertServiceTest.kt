package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithId
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.testsuite.TestSuiteRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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

    @Test fun insertsTestSuiteCorrectly() {
        val testSuite = TestSuiteDto("My Test Suite")
        insertService.insertTestSuite(testSuite)

        verify(testSuiteRepository).save(argThat(testSuiteWithName(testSuite.name)))
        verifyNoMoreInteractions(testSuiteRepository)
    }

    @Test fun returnsCorrectTestSuite() {
        val testSuiteDto = TestSuiteDto("MyTestSuite")

        val result = insertService.insertTestSuite(testSuiteDto)

        assertNotNull(result.id)
        assertEquals(testSuiteDto.name, result.name)
        verify(testSuiteRepository).save(argThat(testSuiteWithId(result.id)))
    }
}