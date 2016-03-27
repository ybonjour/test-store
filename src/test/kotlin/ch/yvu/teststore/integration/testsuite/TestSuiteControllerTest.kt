package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Description
import org.hamcrest.Matchers.*
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired


class TestSuiteControllerTest() : BaseIntegrationTest() {

    @Autowired lateinit var testSuiteRepository: TestSuiteRepository

    @Before override fun setUp() {
        super.setUp()
        testSuiteRepository.deleteAll()
    }

    @Test fun storesTestSuiteWithCorrectName() {
        val testSuiteName = "MyTestSuite"

        given().queryParam("name", testSuiteName).`when`().post("/testsuites")

        val tests = testSuiteRepository.findAll()
        assertEquals(1, tests.count())
        assertThat(tests, hasItem(testSuiteWithName(testSuiteName)))
    }

    @Test fun returnsIdOfStoredTestSuite() {
        given().queryParam("name", "MyTestSuite").`when`().post("/testsuites").then()
                .assertThat()
                .statusCode(201)
                .body("id", not(isEmptyOrNullString()))
    }

    private fun testSuiteWithName(name: String): TypeSafeMatcher<TestSuite> = object : TypeSafeMatcher<TestSuite>() {
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