package ch.yvu.teststore.common

import com.datastax.driver.core.SimpleStatement
import com.datastax.driver.core.Statement

class SimpleQuery(val queryString:String, vararg val values: Any) : Query {
    override fun createStatement(): Statement = SimpleStatement(queryString, values)
}