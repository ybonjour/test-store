package ch.yvu.teststore.integration.insert.dto

import ch.yvu.teststore.insert.dto.ResultDto
import ch.yvu.teststore.insert.dto.RunDto
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat

class RunDtoTest {
    companion object {
        val timeString = "2000-10-31T01:30:00.001-0500"
        val time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").parse(timeString)
        val revision = "abc123"
        val testName = "MyTest"
        val testName2 = "MyTest2"
        val retryNum = 1
        val passed = true
    }

    @Test fun parseRunWithNoTestResults() {
        val json = "{\"revision\": \"${revision}\", \"time\": \"${timeString}\", \"results\": []}"

        val runDto = RunDto.fromJson(json)

        val expectedDto = RunDto(
                revision = revision,
                time = time,
                results = listOf()
        )

        assertEquals(expectedDto, runDto)
    }

    @Test fun parseRunWithOneTestResult() {

        val json = "{\"revision\": \"${revision}\", \"time\": \"${timeString}\", \"results\": [{\"testName\": \"${testName}\", \"retryNum\": ${retryNum}, \"passed\":${passed}}]}"

        val runDto = RunDto.fromJson(json)

        val expectedDto = RunDto(
                revision = revision,
                time = time,
                results = listOf(
                        ResultDto(testName = testName, retryNum = retryNum, passed = passed)
                ))

        assertEquals(expectedDto, runDto)
    }

    @Test fun parseRunWithMultipleTestResult() {

        val json = "{\"revision\": \"${revision}\", \"time\": \"${timeString}\", \"results\": [{\"testName\": \"${testName}\", \"retryNum\": ${retryNum}, \"passed\":${passed}}, {\"testName\": \"${testName2}\", \"retryNum\": ${retryNum}, \"passed\":${passed}}]}"

        val runDto = RunDto.fromJson(json)

        val expectedDto = RunDto(
                revision = revision,
                time = time,
                results = listOf(
                        ResultDto(testName = testName, retryNum = retryNum, passed = passed),
                        ResultDto(testName = testName2, retryNum = retryNum, passed = passed)
                ))

        assertEquals(expectedDto, runDto)
    }
}