package ch.yvu.teststore.result

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ResultTest {

    @Test fun endTimeIsStartTimePlusDuration() {
        val startMillis = 1L
        val result = Result()
        result.time = Date(startMillis)
        result.durationMillis = 10L

        val endTime = result.endTime()

        assertEquals(Date(startMillis + result.durationMillis!!), endTime)
    }

    @Test fun endTimeIsStartTimeIfDurationIsNull() {
        val result = Result()
        result.time = Date(1L)

        val endTime = result.endTime()

        assertNotNull(endTime)
        assertEquals(result.time, endTime)
    }

    @Test fun endTimeIsNullIfTimeIsNull() {
        val result = Result()

        val endTime = result.endTime()

        assertNull(endTime)
    }

}