package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import ch.yvu.teststore.insert.dto.TestSuiteDto
import ch.yvu.teststore.integration.result.ResultControllerTest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class JsonParsingTest {
    lateinit var mapper: ObjectMapper

    @Before
    fun setUp() {
        mapper = ObjectMapper()
        mapper.dateFormat = SimpleDateFormat(ResultControllerTest.isoFormat)
    }

    @Test
    fun canParseTestSuite() {
        val testSuiteDto = TestSuiteDto(name = "MyTestSuite")
        val json = mapper.writeValueAsString(testSuiteDto)

        val actualDto = mapper.readValue(json, TestSuiteDto::class.java)

        assertEquals(testSuiteDto, actualDto)
    }

    @Test
    fun canParseRun() {
        val runDto = RunDto(
                revision = "abc123",
                time = Date(1)
        )

        val json = mapper.writeValueAsString(runDto)

        val actualDto = mapper.readValue(json, RunDto::class.java)

        assertEquals(runDto, actualDto)
    }

    @Test
    fun canParseResult() {
        val resultDto = ResultDto(
                testName="MyTest",
                retryNum = 0,
                passed = true,
                durationMillis = 10,
                time = Date(1)
        )
        val json = mapper.writeValueAsString(resultDto)

        val actualDto = mapper.readValue(json, ResultDto::class.java)

        assertEquals(resultDto, actualDto)
    }

    @Test
    fun canParseResultWithStacktrace() {
        val resultDto = ResultDto(
                testName="MyTest",
                retryNum = 0,
                passed = false,
                durationMillis = 10,
                time = Date(1),
                stackTrace = "Some stacktrace"
        )
        val json = mapper.writeValueAsString(resultDto)

        val actualDto = mapper.readValue(json, ResultDto::class.java)

        assertEquals(resultDto, actualDto)
    }
}