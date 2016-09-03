package ch.yvu.teststore.common

import ch.yvu.teststore.common.PagedResultFetcher.Companion.defaultFetchSize
import com.datastax.driver.core.PagingState
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper

class PagedResultFetcher<T>(val session: Session, val mapper: Mapper<T>, val fetchSize: Int = defaultFetchSize) {
    companion object {
        val defaultFetchSize = 200
    }

    fun fetch(query: Query, page: String? = null): Page<T> {
        val statement = query.createStatement()
        statement.fetchSize = fetchSize
        if(page != null) {
            statement.setPagingState(PagingState.fromString(page))
        }
        val resultSet = session.execute(statement)
        val result = mapper.map(resultSet)

        var results = emptyList<T>()

        (1..resultSet.availableWithoutFetching).forEach {
            results += result.one()
        }

        return Page(results, resultSet.executionInfo?.pagingState?.toString())
    }

}