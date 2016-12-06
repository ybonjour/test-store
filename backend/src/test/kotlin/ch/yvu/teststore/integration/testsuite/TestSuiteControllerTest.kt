package ch.yvu.teststore.integration.testsuite

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.matchers.TestSuiteMatchers.testSuiteWithName
import ch.yvu.teststore.result.Result
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.Run
import ch.yvu.teststore.run.RunRepository
import ch.yvu.teststore.run.overview.RunStatistics.RunResult.PASSED
import ch.yvu.teststore.run.overview.RunStatistics.RunResult.UNKNOWN
import ch.yvu.teststore.testsuite.TestSuite
import ch.yvu.teststore.testsuite.TestSuiteRepository
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType.JSON
import org.hamcrest.Matchers.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.UUID.randomUUID

class TestSuiteControllerTest() : BaseIntegrationTest() {

    @Autowired lateinit var testSuiteRepository: TestSuiteRepository
    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository;

    @Before override fun setUp() {
        super.setUp()
        testSuiteRepository.deleteAll()
    }

    @Test fun storesTestSuiteCorrectly(){
        given()
                .contentType(JSON)
                .body("{\"name\": \"MyTestSuite\"}").post("/testsuites")

        val testSuites = testSuiteRepository.findAll()

        assertEquals(1, testSuites.count())
        assertThat(testSuites, hasItem(testSuiteWithName("MyTestSuite")))
    }

    @Test fun returnsIdOfCreatedTestSuite() {
        given()
                .contentType(JSON)
                .body("{\"name\": \"MyTestSuite\"}").post("/testsuites").then()
                .assertThat()
                .statusCode(201)
                .body("id", not(isEmptyOrNullString()))
    }

    @Test fun getTestSuitesReturnsLastTestResult() {
        val testSuite = TestSuite(randomUUID(), "MyTestSuite")
        val run = Run(randomUUID(), testSuite.id, "abc-123", Date());
        val result = Result(run.id, "MyTest", 0, true, 2, Date())
        testSuiteRepository.save(testSuite)
        runRepository.save(run)
        resultRepository.save(result)

        given()
                .get("/testsuites")
                .then()
                .statusCode(200)
                .body("[0].testSuite.name", equalTo(testSuite.name))
                .body("[0].lastRunResult", equalTo(PASSED.toString()))
    }

    @Test fun getTestSuiteReturnsLastRunResultUnknownIfNoRunIsPresent() {
        val testSuite = TestSuite(randomUUID(), "MyTestSuite")
        testSuiteRepository.save(testSuite)

        given().get("/testsuites").then().body("[0].lastRunResult", equalTo(UNKNOWN.toString()))
    }

    @Test fun getTestSuitesReturnsTestSuites() {
        val testSuite1 = TestSuite(randomUUID(), "MyTestSuite1")
        val testSuite2 = TestSuite(randomUUID(), "MyTestSuite2")
        testSuiteRepository.save(testSuite1)
        testSuiteRepository.save(testSuite2)

        given()
                .get("/testsuites")
                .then()
                .statusCode(200)
                .body("[0].testSuite.name", equalTo(testSuite1.name))
                .body("[1].testSuite.name", equalTo(testSuite2.name))
    }

    @Test fun getTestSuiteReturnsTestSuite() {
        val testSuite = TestSuite(randomUUID(), "MyTestSuite")
        val run = Run(randomUUID(), testSuite.id, "abc-123", Date());
        val result = Result(run.id, "MyTest", 0, true, 2, Date())
        testSuiteRepository.save(testSuite)
        runRepository.save(run)
        resultRepository.save(result)

        given()
                .get("/testsuites/${testSuite.id}")
                .then()
                .statusCode(200)
                .body("testSuite.id", equalTo(testSuite.id.toString()))
                .body("testSuite.name", equalTo(testSuite.name))
                .body("lastRunResult", equalTo(PASSED.toString()))
    }

    @Test fun getTestSuiteReturnsUnknownLastTestResultIfNoRunsExist() {
        val testSuite = TestSuite(randomUUID(), "MyTestSuite")
        testSuiteRepository.save(testSuite)

        given()
                .get("/testsuites/${testSuite.id}")
                .then()
                .statusCode(200)
                .body("lastRunResult", equalTo(UNKNOWN.toString()))
    }

    @Test fun getTestSuiteReturns404IfNoMatchingTestSuiteExists() {
        given()
                .get("/testsuites/${randomUUID()}")
                .then()
                .statusCode(404)
    }
}