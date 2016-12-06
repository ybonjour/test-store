package ch.yvu.teststore.run.overview

import ch.yvu.teststore.run.overview.RunStatistics.RunResult.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RunResultTest {
    @Test fun passedIsMoreSevereThanUnknown() {
        assertTrue(PASSED.isMoreSevere(UNKNOWN))
    }

    @Test fun passedWithRetriesIsMoreSevereThanPassed() {
        assertTrue(PASSED_WITH_RETRIES.isMoreSevere(PASSED))
    }

    @Test fun failedIsMoreSevereThanPassedWithRetries() {
        assertTrue(FAILED.isMoreSevere(PASSED_WITH_RETRIES))
    }

    @Test fun runResultIsNotMoreSevereThanItself() {
        assertFalse(PASSED.isMoreSevere(PASSED))
    }
}
