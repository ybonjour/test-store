package ch.yvu.teststore.integration.insert

import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.XML
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID.randomUUID

class InsertControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun canInsertXmlResults() {
        val run = randomUUID()

        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\"/>\n    </testsuite>\n</testsuites>"

        given()
                .contentType(XML)
                .body(xml)
                .post("/runs/${run.toString()}/results")
                .then().assertThat().statusCode(200)

        assertEquals(1, resultRepository.count())
    }
}