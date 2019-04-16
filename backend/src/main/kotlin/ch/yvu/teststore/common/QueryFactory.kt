package ch.yvu.teststore.common

open class QueryFactory {
    open fun createQuery(query: String, vararg values: Any?): Query {
        return SimpleQuery(query, *values)
    }
}