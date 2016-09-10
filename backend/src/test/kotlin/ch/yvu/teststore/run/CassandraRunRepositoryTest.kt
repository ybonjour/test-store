package ch.yvu.teststore.run

import ch.yvu.teststore.common.*
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

    companion object {
        val query = SimpleQuery("SELECT * FROM foo")
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date())
    }

    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var result: Result<Run>

    @Mock
    lateinit var pagedResultFetcher: PagedResultFetcher<Run>

    @Mock
    lateinit var maxRowsResultFetcher: MaxRowsResultFetcher<Run>

    @Mock
    lateinit var mapper: Mapper<Run>

    @Mock
    lateinit var queryFactory: QueryFactory

    lateinit var repository: CassandraRunRepository

    @Before fun setUp() {
        initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(Run::class.java)).thenReturn(mapper)
        `when`(queryFactory.createQuery(anyString(), anyCollection())).thenReturn(query)

        repository = CassandraRunRepository(mappingManager, queryFactory)
        repository.pagedResultFetcher = pagedResultFetcher
        repository.maxRowsResultFetcher = maxRowsResultFetcher
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
        val resultSet = mock(ResultSet::class.java)
        `when`(session.execute(anyString(), any(UUID::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)
        `when`(result.all()).thenReturn(listOf(run))
        val testSuiteId = randomUUID()

        val resultRun = repository.findAllByTestSuiteId(testSuiteId)

        assertEquals(listOf(run), resultRun)
    }

    @Test fun findAllByRunIdPagedReturnsCorrectResult() {
        val page = Page(emptyList<Run>(), null)
        `when`(pagedResultFetcher.fetch(query, null)).thenReturn(page)

        val result = repository.findAllByTestSuiteId(randomUUID(), null)

        assertEquals(page, result)
    }

    @Test fun findAllSendsCorrectQuery() {
        val testSuiteId = randomUUID()

        repository.findAllByTestSuiteId(testSuiteId, null)

        verify(queryFactory).createQuery("SELECT * FROM run WHERE testSuite=?", testSuiteId)
        verify(pagedResultFetcher).fetch(query, null)
    }

    @Test fun findAllPassesOnPageStateCorrectly() {
        val page = "abcdef"

        repository.findAllByTestSuiteId(randomUUID(), page)

        verify(pagedResultFetcher).fetch(query, page)
    }

    @Test fun findAllByRunIdWithMaxRowsSendsCorrectQuery() {
        val maxRows = 10
        `when`(maxRowsResultFetcher.fetch(query, maxRows)).thenReturn(listOf(run))

        val result = repository.findAllByTestSuiteId(run.testSuite!!, maxRows)

        verify(queryFactory).createQuery("SELECT * FROM run WHERE testSuite=?", run.testSuite!!)
        verify(maxRowsResultFetcher).fetch(query, maxRows)
    }

    @Test fun findAllByRunIdWithMaxRowsReturnsCorrectResult() {
        val maxRows = 10
        `when`(maxRowsResultFetcher.fetch(query, maxRows)).thenReturn(listOf(run))

        val result = repository.findAllByTestSuiteId(run.testSuite!!, maxRows)

        assertEquals(listOf(run), result)
    }
}