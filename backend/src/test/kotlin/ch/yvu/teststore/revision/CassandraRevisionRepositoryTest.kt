package ch.yvu.teststore.revision

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
import org.mockito.MockitoAnnotations
import java.util.*
import java.util.UUID.randomUUID

class CassandraRevisionRepositoryTest {
    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var result: Result<Revision>

    @Mock
    lateinit var mapper: Mapper<Revision>

    lateinit var repository: CassandraRevisionRepository

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(Revision::class.java)).thenReturn(mapper)

        repository = CassandraRevisionRepository(mappingManager)
    }

    @Test fun findAllByRunIdSendsCorrectQuery() {
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(emptyList<Revision>())
        val runId = randomUUID();

        repository.findAllByRunId(runId)

        verify(session).execute("SELECT * FROM revision WHERE run=?", runId)
    }

    @Test fun findAllByRunIdReturnsCorrectResult() {
        val runId = randomUUID()
        val revision = Revision(runId, Date(1), "abc-123", "some author", "Some comment", "http://some-url.org")
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(listOf(revision))

        val resultRevision = repository.findAllByRunId(runId)

        assertEquals(listOf(revision), resultRevision)
    }

}

