package ch.yvu.teststore.matchers

import ch.yvu.teststore.result.Result
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.*

object ResultMatchers {

    fun resultWith(runId: Matcher<UUID>, testId: Matcher<UUID>, retryNum: Int, passed: Boolean) = object : TypeSafeMatcher<Result>() {
        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Result with run ")
            description.appendDescriptionOf(runId)
            description.appendText(", test ")
            description.appendDescriptionOf(testId)
            description.appendText(", retryNum ${retryNum}, passed ${passed}")
        }

        override fun matchesSafely(item: Result?): Boolean {
            if (item == null) return false

            return runId.matches(item.run)
                    && testId.matches(item.test)
                    && item.retryNum == retryNum
                    && item.passed == passed
        }
    }
}