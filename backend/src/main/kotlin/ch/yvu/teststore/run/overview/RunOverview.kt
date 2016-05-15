package ch.yvu.teststore.run.overview

import ch.yvu.teststore.run.Run

data class RunOverview(val run: Run, val result: RunResult, val totalDurationMillis: Long) {
    enum class RunResult(val severity: Int) {
        UNKNOWN(0), PASSED(1), PASSED_WITH_RETRIES(2), FAILED(3);

        fun isMoreSevere(other: RunResult): Boolean {
            return severity > other.severity
        }
    }
}