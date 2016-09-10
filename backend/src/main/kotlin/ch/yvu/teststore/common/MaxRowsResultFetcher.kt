package ch.yvu.teststore.common

// This class is needed, because Cassandra does not guarantee to
// fetch the exact amount of rows as specified in statement.fetchSize.
// Therefore we have to ensure that we have the exact amount of rows.
open class MaxRowsResultFetcher<T>(val pagedResultFetcher: PagedResultFetcher<T>) {

    open fun fetch(query: Query, maxRows: Int): List<T> {
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