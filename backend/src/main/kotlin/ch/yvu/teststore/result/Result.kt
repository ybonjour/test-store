package ch.yvu.teststore.result

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.PartitionKey
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "result")
data class Result(
        @PartitionKey(0) var run: UUID?,
        @PartitionKey(1) var testName: String?,
        @PartitionKey(2) var retryNum: Int?,
        var passed: Boolean?,
        var durationMillis: Long?,
        var time: Date?,
        var stackTrace: String? = null,
        var log: String? = null,
        var failureReason: String? = null) : Model {
    constructor() : this(null, null, null, null, null, null) {
    }

    fun isRetry(): Boolean {
        return retryNum!! > 0
    }

    fun setPassed(passed: Boolean) {
        this.passed = passed
    }

    fun isPassed(): Boolean {
        synchronized(this, {
            if (passed == null) return false
            else return passed!!
        })
    }

    fun endTime(): Date? {
        if(this.time == null) return null

        return Date(this.time!!.time + (this.durationMillis ?: 0))
    }
}
