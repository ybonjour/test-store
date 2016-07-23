package ch.yvu.teststore.run

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.datastax.driver.mapping.Result
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import java.util.*
import java.util.UUID.randomUUID

class CassandraRunRepositoryTest {

    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var result: Result<Run>

    @Mock
    lateinit var mapper: Mapper<Run>

    lateinit var repository: CassandraRunRepository

    @Before fun setUp() {
        initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(Run::class.java)).thenReturn(mapper)

        repository = CassandraRunRepository(mappingManager)
    }

    @Test fun findAllByRunIdSendsCorrectQuery() {
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(emptyList<Run>())
        val testSuiteId = randomUUID()

        repository.findAllByTestSuiteId(testSuiteId)

        verify(session).execute("SELECT * FROM run WHERE testSuite=?", testSuiteId)
    }

    @Test fun findAllByRunIdReturnsCorrectResult() {
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(listOf(run))
        val testSuiteId = randomUUID()

        val resultRun = repository.findAllByTestSuiteId(testSuiteId)

        assertEquals(listOf(run), resultRun)
    }
}