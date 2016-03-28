package ch.yvu.teststore.matchers

import ch.yvu.teststore.result.Result
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher
import java.util.*

object ResultMatchers {

    fun resultWith(run: UUID, test: UUID, retryNum: Int, passed: Boolean) = object : TypeSafeMatcher<Result>() {
        override fun describeTo(description: Description?) {
            if (description == null) return
            description.appendText("Result with run ${run}, test ${test}, retryNum ${retryNum}, passed ${passed}")
        }

        override fun matchesSafely(item: Result?): Boolean {
            if (item == null) return false

            return item.run == run && item.test == test && item.retryNum == retryNum && item.passed == passed
        }
    }
}