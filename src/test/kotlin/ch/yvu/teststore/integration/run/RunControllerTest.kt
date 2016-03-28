package ch.yvu.teststore.integration.run

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.RunMatchers.runWith
import ch.yvu.teststore.run.RunRepository
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.text.SimpleDateFormat
import java.util.*
import java.util.UUID.randomUUID

class RunControllerTest : BaseIntegrationTest() {
    companion object {
        val isoFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
        val nowString = SimpleDateFormat(isoFormat).format(Date())
        val now = SimpleDateFormat(isoFormat).parse(nowString)
    }

    @Autowired lateinit var runRepository: RunRepository;

    @Before override fun setUp() {
        super.setUp()
        runRepository.deleteAll()
    }

    @Test fun createRunReturnsCorrectStatusCode() {
        given().queryParam("testSuite", randomUUID())
                .queryParam("revision", "abcd123")
                .queryParam("time", nowString)
                .post("/runs")
                .then().assertThat().statusCode(201)
    }

    @Test fun storesRunWithCorrectRevisionAndTestSuite() {
        val testSuite = randomUUID()
        val revision = "abcd123"
        val time = nowString

        given().queryParam("testSuite", testSuite)
                .queryParam("revision", revision)
                .queryParam("time", time)
                .post("/runs")

        val runs = runRepository.findAll()
        assertEquals(1, runs.count())
        assertThat(runs, hasItem(runWith(revision = revision, testSuite = testSuite, time = now)))
    }

    @Test fun runIdIsReturnedWhenCreatingARun() {
        given().queryParam("testSuite", randomUUID().toString())
                .queryParam("revision", "abcd123")
                .queryParam("time", nowString)
                .post("/runs")
                .then().assertThat().body("id", not(isEmptyOrNullString()))
    }
}



