package ch.yvu.teststore.common

import com.datastax.driver.core.PagingState
import com.datastax.driver.core.Session
import com.datastax.driver.mapping.Mapper

open class PagedResultFetcher<T>(val session: Session, val mapper: Mapper<T>) {
    companion object {
        val defaultFetchSize = 200
    }

    open fun fetch(query: Query, page: String? = null, fetchSize: Int? = null): Page<T> {
        val statement = query.createStatement()
        statement.fetchSize = fetchSize ?: defaultFetchSize
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