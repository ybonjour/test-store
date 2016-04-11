package ch.yvu.teststore.insert.dto

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class JsonConversionTest {
    @Test fun convertRun() {
        val mapper = ObjectMapper()
        val runDto = RunDto(
                revision = "abc123",
                time = Date(),
                results = listOf(ResultDto(
                        testName = "MyTest",
                        passed = true,
                        durationMillis = 10
                ))
        )
        val json = mapper.writeValueAsString(runDto)

        val result = mapper.readValue(json, RunDto::class.java)

        assertEquals(runDto, result)
    }
}
