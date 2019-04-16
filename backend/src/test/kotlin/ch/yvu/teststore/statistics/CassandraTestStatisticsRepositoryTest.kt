package ch.yvu.teststore.statistics

import ch.yvu.teststore.common.Page
import ch.yvu.teststore.common.PagedResultFetcher
import ch.yvu.teststore.common.QueryFactory
import ch.yvu.teststore.common.SimpleQuery
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.MappingManager
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import java.util.UUID.randomUUID

class CassandraTestStatisticsRepositoryTest {

    companion object {
        val query = SimpleQuery("SELECT * from foo")
    }

    @Mock
    lateinit var mappingManager: MappingManager

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var mapper: Mapper<TestStatistics>

    @Mock
    lateinit var queryFactory: QueryFactory

    @Mock
    lateinit var pagedResultFetcher: PagedResultFetcher<TestStatistics>

    lateinit var repository: CassandraTestStatisticsRepository

    @Before
    fun setUp() {
        initMocks(this)
        `when`(mappingManager.session).thenReturn(session)
        `when`(mappingManager.mapper(TestStatistics::class.java)).thenReturn(mapper)

        `when`(queryFactory.createQuery(anyString(), any())).thenReturn(query)
        repository = CassandraTestStatisticsRepository(mappingManager, queryFactory)
        repository.pagedResultFetcher = pagedResultFetcher
    }

    @Test
    fun findAllByTestSuitePagedReturnsCorrectResult() {
        val page = Page(emptyList<TestStatistics>(), null)
        `when`(pagedResultFetcher.fetch(query, null)).thenReturn(page)

        repository.findAllByTestSuitePaged(randomUUID())
    }

    @Test
    fun findAllByTestSuitePagedPassesPageCorrectly() {
        val page = "abcdef"

        repository.findAllByTestSuitePaged(randomUUID(), page)

        verify(pagedResultFetcher).fetch(query, page)
    }

    @Test
    fun findAllByTestSuitePagedPassesFetchSizeCorrecly() {
        val fetchSize = 2
        repository.findAllByTestSuitePaged(randomUUID(), fetchSize = fetchSize)

        verify(pagedResultFetcher).fetch(query, fetchSize = fetchSize)
    }

    @Test
    fun findAllByTestSuitePagedUsesNullAsDefaultValuesForPageAndFetchSize() {
        repository.findAllByTestSuitePaged(randomUUID())

        verify(pagedResultFetcher).fetch(query, page = null, fetchSize = null)
    }
}