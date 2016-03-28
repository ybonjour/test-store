package ch.yvu.teststore.matchers

import ch.yvu.teststore.run.Run
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.*

object RunMatchers {
    fun runWith(revision: String, testSuite: UUID) = object : TypeSafeMatcher<Run>() {
        override fun matchesSafely(item: Run?): Boolean {
            if (item == null) return false

            return item.revision == revision && item.testSuite == testSuite
        }

        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Run with revision $revision and testSuite $testSuite")
        }
    }
}