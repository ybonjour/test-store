package ch.yvu.teststore.common

import com.datastax.driver.core.SimpleStatement
import com.datastax.driver.core.Statement

open class Query(val queryString:String, vararg val values: Any) {
    open fun createStatement(): Statement = SimpleStatement(queryString, values)
}