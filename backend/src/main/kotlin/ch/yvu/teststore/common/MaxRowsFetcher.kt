package ch.yvu.teststore.common

class MaxRowsFetcher<T>(val pagedResultFetcher: PagedResultFetcher<T>) {

    fun fetch(query: Query, maxRows: Int): List<T> {
        var allResults = emptyList<T>()
        var nextPage: String? = null
        while (allResults.size < maxRows) {
            val page = pagedResultFetcher.fetch(query, page = nextPage, fetchSize = maxRows - allResults.size)
            allResults += page.results
            nextPage = page.nextPage
            if(nextPage == null) break
        }

        return allResults.take(maxRows)
    }

}