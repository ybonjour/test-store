package ch.yvu.teststore.result

ndata class TestWithResults(val testName: String, val results: List<Result> = emptyList()) {
    enum class TestResult {
        PASSED, FAILED, RETRIED
    }

    fun addResult(result: Result): TestWithResults {
        return TestWithResults(testName, results + result)
    }

    fun getTestResult(): TestResult {
        if (results.isEmpty()) return TestResult.PASSED
        val lastResult = results.sortedBy { it.retryNum }.last()

        if (lastResult.isFailed()) return TestResult.FAILED
        else if (lastResult.isRetry()) return TestResult.RETRIED
        else return TestResult.PASSED
    }
}