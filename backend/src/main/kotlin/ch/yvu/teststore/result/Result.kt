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
        var stackTrace: String?) : Model {
    constructor(): this(null, null, null, null, null, null) {}
    constructor(run: UUID?, testName: String?, retryNum:Int?, passed:Boolean?, durationMillis: Long?):
        this(run, testName, retryNum, passed, durationMillis, null);

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
}
