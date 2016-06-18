package ch.yvu.teststore.testsuite.overview

import ch.yvu.teststore.run.overview.RunOverview
import ch.yvu.teststore.testsuite.TestSuite

data class TestSuiteOverview(val testSuite: TestSuite, val lastRunResult: RunOverview.RunResult?) {
}