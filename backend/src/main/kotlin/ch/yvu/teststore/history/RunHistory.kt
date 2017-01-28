package ch.yvu.teststore.history

import ch.yvu.teststore.result.TestWithResults
import java.util.*

data class RunHistory(val revision: String, val runId: UUID, val results: Map<String, TestWithResults.TestResult>,
                      val results2: List<TestWithResults.TestResult> = emptyList()) {
}