package ch.yvu.teststore.run.overview

import ch.yvu.teststore.run.Run

data class RunOverview(val run: Run, val result: RunResult) {
    enum class RunResult {
        UNKNOWN, PASSED, PASSED_WITH_RETRIES, FAILED
    }
}