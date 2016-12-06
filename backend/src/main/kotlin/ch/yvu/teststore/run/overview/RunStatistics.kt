package ch.yvu.teststore.run.overview

import ch.yvu.teststore.run.Run
import java.util.*

data class RunStatistics(val run: Run,
                         val result: RunResult,
                         val totalDurationMillis: Long,
                         val numPassed: Int,
                         val numFailed: Int,
                         val firstResultTime: Date?,
                         val lastResultTime: Date?) {

    constructor(run: Run,
                result: String,
                totalDurationMillis: Long,
                numPassed: Int,
                numFailed: Int,
                firstResultTime: Date?,
                lastResultTime: Date?) :
            this(run, RunResult.valueOf(result), totalDurationMillis, numPassed, numFailed, firstResultTime, lastResultTime) {
    }


    enum class RunResult(val severity: Int) {
        UNKNOWN(0), PASSED(1), PASSED_WITH_RETRIES(2), FAILED(3);

        fun isMoreSevere(other: RunResult): Boolean {
            return severity > other.severity
        }
    }
}