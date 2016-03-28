package ch.yvu.teststore.matchers

import ch.yvu.teststore.test.Test
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object TestMatchers {
    fun testWithName(name: String) = object : TypeSafeMatcher<Test>() {
        override fun matchesSafely(item: Test?): Boolean {
            if (item == null) return false

            return item.name == name
        }

        override fun describeTo(description: Description?) {
            if (description == null) return

            description.appendText("Test with name ${name}")
        }
    }
}