package ch.yvu.teststore.run.overview

import ch.yvu.teststore.common.Model
import com.datastax.driver.mapping.annotations.Table
import java.util.*

@Table(name = "run_statistics")
data class RunStatistics(val run: UUID?,
                         val result: RunResult?,
                         val totalDurationMillis: Long?,
                         val numPassed: Int?,
                         val numFailed: Int?,
                         val firstResultTime: Date?,
                         val lastResultTime: Date?) : Model {

    constructor() : this(null, null, null, null, null, null, null) {
    }

    constructor(run: UUID,
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