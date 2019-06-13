package ch.yvu.teststore.run

import ch.yvu.teststore.common.*
import ch.yvu.teststore.integration.run.runInstance
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
import java.util.UUID.randomUUID

class CassandraRunRepositoryTest {

    companion object {
        val query = SimpleQuery("SELECT * FROM foo")
        val run = runInstance()
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
        `when`(queryFactory.createQuery(anyString(), any())).thenReturn(query)

        repository = CassandraRunRepository(mappingManager, queryFactory)
        repository.pagedResultFetcher = pagedResultFetcher
        repository.maxRowsResultFetcher = maxRowsResultFetcher
    }

    @Test fun findAllByTestSuiteIdPagedReturnsCorrectResult() {
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


    @Test fun findAllPassesOnFetchSizeCorrectly() {
        val fetchSize = 2

        repository.findAllByTestSuiteId(randomUUID(), fetchSize = fetchSize)

        verify(pagedResultFetcher).fetch(query, fetchSize = fetchSize)
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