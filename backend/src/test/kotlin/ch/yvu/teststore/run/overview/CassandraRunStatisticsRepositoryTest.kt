package ch.yvu.teststore.run.overview

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import com.datastax.driver.mapping.Result
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.*

class CassandraRunStatisticsRepositoryTest {
    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var result: Result<RunStatistics>

    @Mock
    lateinit var mapper: Mapper<RunStatistics>

    lateinit var repository: CassandraRunStatisticsRepository

    @Before fun setUp() {
        MockitoAnnotations.initMocks(this)
        Mockito.`when`(mappingManager.session).thenReturn(session)
        Mockito.`when`(mappingManager.mapper(RunStatistics::class.java)).thenReturn(mapper)

        repository = CassandraRunStatisticsRepository(mappingManager)
    }

    @Test fun findByRunIdSendsCorrectQuery() {
        val resultSet = Mockito.mock(ResultSet::class.java)
        Mockito.`when`(session.execute(Mockito.anyString(), Mockito.any(UUID::class.java))).thenReturn(resultSet)
        Mockito.`when`(mapper.map(resultSet)).thenReturn(result)
        Mockito.`when`(result.one()).thenReturn(RunStatistics())
        val runId = UUID.randomUUID()

        repository.findByRunId(runId)

        Mockito.verify(session).execute("SELECT * FROM run_statistics WHERE run=?", runId)
    }

    @Test fun findByRunIdReturnsRunStatistics() {
        val runStatistics = RunStatistics()
        val resultSet = Mockito.mock(ResultSet::class.java)
        Mockito.`when`(session.execute(Mockito.anyString(), Mockito.any(UUID::class.java))).thenReturn(resultSet)
        Mockito.`when`(mapper.map(resultSet)).thenReturn(result)
        Mockito.`when`(result.one()).thenReturn(runStatistics)
        val runId = UUID.randomUUID()

        val actualRunStatistics = repository.findByRunId(runId)

        assertEquals(runStatistics, actualRunStatistics)
    }

    @Test fun findByRunIdReturnsNullIfNoRunStatisticsFound() {
        val resultSet = Mockito.mock(ResultSet::class.java)
        Mockito.`when`(session.execute(Mockito.anyString(), Mockito.any(UUID::class.java))).thenReturn(resultSet)
        Mockito.`when`(resultSet.isExhausted).thenReturn(true);
        val runId = UUID.randomUUID()

        val runStatistics = repository.findByRunId(runId)

        assertNull(runStatistics)
    }
}