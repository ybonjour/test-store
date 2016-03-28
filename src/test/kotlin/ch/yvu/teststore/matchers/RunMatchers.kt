package ch.yvu.teststore.matchers

import ch.yvu.teststore.run.Run
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.*

object RunMatchers {
    fun runWith(revision: String, testSuite: UUID, time: Date) = object : TypeSafeMatcher<Run>() {
        override fun matchesSafely(item: Run?): Boolean {
            if (item == null) return false

            var match = item.revision == revision
            match = match && (item.testSuite == testSuite)
            match = match && (time.equals(item.time))
            return match
        }

        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Run with revision $revision, testSuite $testSuite and time $time")
        }
    }

    fun runWithId(id: UUID) = object : TypeSafeMatcher<Run>() {
        override fun matchesSafely(item: Run?): Boolean {
            if (item == null) return false

            return item.id == id
        }

        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Run with id $id")
        }
    }
}