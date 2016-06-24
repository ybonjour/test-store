package ch.yvu.teststore.history

import ch.yvu.teststore.result.TestWithResults

data class RunHistory(val revision: String, val results: Map<String, TestWithResults.TestResult>) {
}