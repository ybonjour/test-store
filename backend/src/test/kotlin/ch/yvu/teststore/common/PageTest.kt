package ch.yvu.teststore.common

import org.junit.Assert.assertEquals
import org.junit.Test

class PageTest {

    @Test fun mapMapsPageContent() {
        val original = listOf(1, 2, 3)
        val page = Page(original, null)

        val resultingPage = page.map { 2 * it }

        assertEquals(listOf(2, 4, 6), resultingPage.results)
    }

    @Test fun mapLeavsNextPageUnchanged() {
        val nextPage = "abcde"
        val page = Page(emptyList<Int>(), nextPage)

        val resultingPage = page.map { 2 * it }

        assertEquals(nextPage, resultingPage.nextPage)
    }
}