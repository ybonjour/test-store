package ch.yvu.teststore.integration.test

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.test.Test
import ch.yvu.teststore.test.TestRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Description
import org.hamcrest.Matchers
import org.hamcrest.Matchers.hasItem
import org.hamcrest.TypeSafeMatcher
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.springframework.beans.factory.annotation.Autowired

class TestControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var testRepository: TestRepository

    @Before override fun setUp() {
        super.setUp()
        testRepository.deleteAll()
    }

    @org.junit.Test fun createTestReturnsCorrectStatusCode() {
        given().queryParam("name", "ATest").post("/tests").then().assertThat().statusCode(201)
    }

    @org.junit.Test fun createTestStoresTestWithCorrectName() {
        val name = "ATest"

        given().queryParam("name", name).post("/tests")

        val tests = testRepository.findAll()
        assertEquals(1, tests.count())
        assertThat(tests, hasItem(testWithName(name)))
    }

    private fun testWithName(name: String) = object : TypeSafeMatcher<Test>() {
        override fun matchesSafely(item: Test?): Boolean {
            if(item == null) return false

            return item.name == name
        }

        override fun describeTo(description: Description?) {
            if(description == null) return

            description.appendText("Test with name ${name}")
        }
    }
}