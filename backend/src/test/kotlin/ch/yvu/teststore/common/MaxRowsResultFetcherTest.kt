package ch.yvu.teststore.common

import ch.yvu.teststore.run.Run
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import java.util.*
import java.util.UUID.randomUUID

class MaxRowsResultFetcherTest {
    companion object {
        val query = SimpleQuery("SELECT foo")
        val run = Run(randomUUID(), randomUUID(), "abc-123", Date(1))
        val run2 = Run(randomUUID(), randomUUID(), "abc-124", Date(2))
        val run3 = Run(randomUUID(), randomUUID(), "abc-125", Date(3))
    }

    @Mock
    lateinit var pagedResultFetcher: PagedResultFetcher<Run>

    lateinit var maxRowsResultFetcher: MaxRowsResultFetcher<Run>

    @Before fun setUp() {
        initMocks(this)
        maxRowsResultFetcher = MaxRowsResultFetcher(pagedResultFetcher)
    }

    @Test fun fetchesAtLeastOnePageWithCorrectFetchSizeFromPageFetcher() {
        val maxRows = 1
        val page = Page(listOf(run), null)
        `when`(pagedResultFetcher.fetch(query, null, maxRows)).thenReturn(page)
        val result = maxRowsResultFetcher.fetch(query, maxRows)

        assertEquals(listOf(run), result)
    }

    @Test fun fetchesNextPageCorrectlyIfNotEnoughResultsFetched() {
        val maxRows = 2
        val nextPage = "1234"
        val page = Page(listOf(run), nextPage)
        `when`(pagedResultFetcher.fetch(query, null, maxRows)).thenReturn(page)
        `when`(pagedResultFetcher.fetch(query, nextPage, 1)).thenReturn(page)

        maxRowsResultFetcher.fetch(query, maxRows)

        verify(pagedResultFetcher).fetch(query, null, maxRows)
        verify(pagedResultFetcher).fetch(query, nextPage, 1)
    }

    @Test fun concatenatesResultsOfMultiplePagesCorrectly() {
        val maxRows = 2
        val nextPage = "1234"
        val page1 = Page(listOf(run), nextPage)
        val page2 = Page(listOf(run2), nextPage)
        `when`(pagedResultFetcher.fetch(query, null, maxRows)).thenReturn(page1)
        `when`(pagedResultFetcher.fetch(query, nextPage, 1)).thenReturn(page2)

        val result = maxRowsResultFetcher.fetch(query, maxRows)

        assertEquals(listOf(run, run2), result)
    }

    @Test fun fetches3PagesCorrectly() {
        val maxRows = 3
        val nextPage1 = "1234"
        val nextPage2 = "5678"
        val page1 = Page(listOf(run), nextPage1)
        val page2 = Page(listOf(run2), nextPage2)
        val page3 = Page(listOf(run3), null)
        `when`(pagedResultFetcher.fetch(query, null, 3)).thenReturn(page1)
        `when`(pagedResultFetcher.fetch(query, nextPage1, 2)).thenReturn(page2)
        `when`(pagedResultFetcher.fetch(query, nextPage2, 1)).thenReturn(page3)

        maxRowsResultFetcher.fetch(query, maxRows)

        verify(pagedResultFetcher).fetch(query, null, 3)
        verify(pagedResultFetcher).fetch(query, nextPage1, 2)
        verify(pagedResultFetcher).fetch(query, nextPage2, 1)
    }

    @Test fun returnsOnlyFirstMaxRowsIfPageHasMoreRows() {
        val maxRows = 1
        val page = Page(listOf(run, run2), null)
        `when`(pagedResultFetcher.fetch(query, null, maxRows)).thenReturn(page)

        val result = maxRowsResultFetcher.fetch(query, maxRows)

        assertEquals(listOf(run), result)
    }

    @Test fun returnsAllRowsIfDatabaseHasLessThanMaxRows() {
        val maxRows = 2
        val page = Page(listOf(run), null)
        `when`(pagedResultFetcher.fetch(query, null, maxRows)).thenReturn(page)

        val result = maxRowsResultFetcher.fetch(query, maxRows)

        assertEquals(listOf(run), result)
    }
}