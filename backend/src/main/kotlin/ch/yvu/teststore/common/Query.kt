package ch.yvu.teststore.common

import com.datastax.driver.core.Statement

interface Query {
    fun createStatement(): Statement
}