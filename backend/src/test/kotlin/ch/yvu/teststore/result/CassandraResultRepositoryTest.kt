package ch.yvu.teststore.result

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import java.util.*
import java.util.UUID.randomUUID

class CassandraResultRepositoryTest {

    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var result: com.datastax.driver.mapping.Result<Result>

    @Mock
    lateinit var mapper: Mapper<Result>

    lateinit var repository: CassandraResultRepository

    @Before fun setUp() {
        initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(Result::class.java)).thenReturn(mapper)

        repository = CassandraResultRepository(mappingManager)
    }

    @Test fun findAllByRunIdSendsCorrectQuery() {
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(emptyList<Result>())
        val runId = randomUUID()

        repository.findAllByRunId(runId)

        verify(session).execute("SELECT * FROM result WHERE run=?", runId)
    }

    @Test fun findAllByRunIdReturnsCorrectResult() {
        val runId = randomUUID()
        val aResult = Result(runId, "SomeTest", 0, true, 203)
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(listOf(aResult))

        val actual = repository.findAllByRunId(runId)

        assertEquals(listOf(aResult), actual)
    }

}