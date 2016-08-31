package ch.yvu.teststore.integration.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.integration.BaseIntegrationTest
import ch.yvu.teststore.result.ResultRepository
import ch.yvu.teststore.run.RunRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType.JSON
import com.jayway.restassured.http.ContentType.XML
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.UUID.randomUUID

class InsertControllerTest : BaseIntegrationTest() {

    @Autowired lateinit var runRepository: RunRepository
    @Autowired lateinit var resultRepository: ResultRepository

    @Before override fun setUp() {
        super.setUp()
        resultRepository.deleteAll()
        runRepository.deleteAll()
    }

    @Test fun canInsertARun() {
        val testSuite = randomUUID()
        val runDto = RunDto(
                revision = "abc123",
                time = Date(),
                results = listOf(ResultDto(
                        testName = "MyTest",
                        passed = true,
                        durationMillis = 10,
                        time = Date(1)
                ))
        )

        val mapper = ObjectMapper()
        val json = mapper.writeValueAsString(runDto)

        given()
                .contentType(JSON)
                .body(json)
                .post("/testsuites/${testSuite.toString()}/runs")
                .then().assertThat().statusCode(200)

        assertEquals(1, runRepository.count())
        assertEquals(1, resultRepository.count())
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