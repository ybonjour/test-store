package ch.yvu.teststore.matchers

import ch.yvu.teststore.testsuite.TestSuite
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object TestSuiteMatchers {
    fun testSuiteWithName(name: String): TypeSafeMatcher<TestSuite> = object : TypeSafeMatcher<TestSuite>() {
        override fun matchesSafely(item: TestSuite?): Boolean {
            if (item == null) return false
            return item.name == name
        }

        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Test with name $name")
        }
    }
}