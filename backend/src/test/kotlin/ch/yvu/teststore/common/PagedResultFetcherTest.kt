package ch.yvu.teststore.common

import ch.yvu.teststore.run.Run
import com.datastax.driver.core.ExecutionInfo
import com.datastax.driver.core.PagingState
import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Session
import com.datastax.driver.core.SimpleStatement
import com.datastax.driver.core.Statement
import com.datastax.driver.mapping.Mapper
import com.datastax.driver.mapping.Result
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import org.mockito.hamcrest.MockitoHamcrest.argThat
import java.util.Date
import java.util.UUID.randomUUID

// This test would ideally be an integration test using the cassandra database directly
// In order to test it we have to mock objects that are not under our control. This is an anti pattern
// Since setting up a database integration test is too costly right now, I decided this is better than no tests.
class PagedResultFetcherTest {

    companion object {
        val testSuite = randomUUID()
        val run = Run(randomUUID(), testSuite, "abc-123", Date(1))
        val query = SimpleQuery("SELECT * FROM run WHERE testSuite=?", testSuite)
        val pagingState = PagingState.fromString("00270010001b000800000156e1d573f11004f3aed39d784b4cb666646f4c5add50f07ffffffdf07ffffffd14f77afb69f7d2b915966de34665f10c0004")
    }

    @Mock
    lateinit var session: Session

    @Mock
    lateinit var mapper: Mapper<Run>

    @Mock
    lateinit var resultSet: ResultSet

    @Mock
    lateinit var result: Result<Run>

    lateinit var fetcher: PagedResultFetcher<Run>

    @Before
    fun setUp() {
        initMocks(this)

        `when`(session.execute(any(Statement::class.java))).thenReturn(resultSet)
        `when`(mapper.map(resultSet)).thenReturn(result)

        fetcher = PagedResultFetcher(session, mapper)
    }

    @Test
    fun executesQuery() {
        givenAResultWithNRows(n = 0)

        fetcher.fetch(query)


        verify(session).execute(argThat(statementWithQuery(query)))
    }

    @Test
    fun returnsResult() {
        givenAResultWithNRows(n = 1)

        `when`(result.isExhausted).thenReturn(false).thenReturn(true)

        val page = fetcher.fetch(query)

        assertEquals(listOf(run), page.results)
    }

    @Test
    fun usesCorrectFetchSize() {
        givenAResultWithNRows(n = 0)

        fetcher.fetch(query)

        verify(session).execute(argThat(statementWithFetchSize(PagedResultFetcher.defaultFetchSize)))
    }

    @Test
    fun overrideDefaultPageSizeIfPageSizeIsPrvoided() {
        val fetchSize = 2
        givenAResultWithNRows(n = 0)

        fetcher.fetch(query, fetchSize = fetchSize)

        verify(session).execute(argThat(statementWithFetchSize(fetchSize)))
    }

    @Test
    fun onlyConsumesAsManyRowsAsFetched() {
        givenAResultWithNRows(n = 2)
        `when`(resultSet.availableWithoutFetching).thenReturn(1)

        fetcher.fetch(query)

        verify(result, times(1)).one()
    }

    @Test
    fun returnsPagingStateInPage() {
        givenAResultWithNRows(n = 1)
        givenResultPagingState(pagingState)

        val page = fetcher.fetch(query)

        assertEquals(pagingState.toString(), page.nextPage)
    }

    @Test
    fun setsProvidedPagingState() {
        givenAResultWithNRows(n = 1)
        val statement = mock(Statement::class.java)
        val query = mock(Query::class.java)
        `when`(query.createStatement()).thenReturn(statement)

        fetcher.fetch(query, pagingState.toString())

        verify(statement).setPagingState(argThat(pagingState(pagingState.toString())))
    }

    private fun givenResultPagingState(pagingState: PagingState) {
        val executionInfo = mock(ExecutionInfo::class.java)
        `when`(executionInfo.pagingState).thenReturn(pagingState)
        `when`(resultSet.executionInfo).thenReturn(executionInfo)
    }

    private fun givenAResultWithNRows(n: Int) {
        var count = 0;
        `when`(result.one()).thenAnswer {
            if (count < n) {
                count += 1
                run
            } else null
        }

        `when`(resultSet.availableWithoutFetching).thenReturn(n)
    }

    private fun pagingState(pagingState: String) = object : TypeSafeMatcher<PagingState>() {
        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendValue(pagingState)
        }

        override fun matchesSafely(item: PagingState?): Boolean {
            if (item == null) return false

            return item.toString() == pagingState
        }
    }

    private fun statementWithFetchSize(fetchSize: Int) = object : TypeSafeMatcher<SimpleStatement>() {
        override fun matchesSafely(item: SimpleStatement?): Boolean {
            if (item == null) return false

            return item.fetchSize == fetchSize
        }

        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Statement with fetchSize ").appendValue(fetchSize)
        }

    }

    private fun statementWithQuery(query: SimpleQuery) = object : TypeSafeMatcher<SimpleStatement>() {

        override fun describeTo(description: Description?) {
            if (description == null) return

            description
                .appendText("Statement with query '").appendValue(query.queryString)
                .appendText("' and values ").appendValue(query.values);
        }

        override fun matchesSafely(statement: SimpleStatement?): Boolean {
            if (statement == null) return false

            var i = 0
            for (value in query.values) {
                if (!statement.getObject(i).equals(value)) {
                    return false
                }
                i += 1
            }

            return query.queryString == statement.queryString

        }
    }
}