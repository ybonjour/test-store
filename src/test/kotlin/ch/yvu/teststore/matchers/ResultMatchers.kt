package ch.yvu.teststore.matchers

import ch.yvu.teststore.result.Result
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.*

object ResultMatchers {

    fun resultWith(runId: Matcher<UUID>, testName: String, retryNum: Int, passed: Boolean, durationMillis: Long) = object : TypeSafeMatcher<Result>() {
        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Result with run ")
            description.appendDescriptionOf(runId)
            description.appendText(", test $testName, retryNum $retryNum, passed $passed, duration $durationMillis")
        }

        override fun matchesSafely(item: Result?): Boolean {
            if (item == null) return false

            return runId.matches(item.run)
                    && item.testName == testName
                    && item.retryNum == retryNum
                    && item.passed == passed
                    && item.durationMillis == durationMillis
        }
    }
}