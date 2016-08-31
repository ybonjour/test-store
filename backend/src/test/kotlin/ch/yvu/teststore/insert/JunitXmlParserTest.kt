package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class JunitXmlParserTest {

    lateinit var timestamp: Date;

    @Before
    fun setUp() {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        timestamp = format.parse("2016-03-31T17:51:02")
    }

    @Test
    fun canParsePassedTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\"/>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = true,
                        time = timestamp,
                        durationMillis = 6
                )
        ), parsed)
    }

    @Test
    fun canParseFailedTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\">\n            <failure message=\"java.lang.AssertionError\" type=\"java.lang.AssertionError\">stacktrace</failure>\n        </testcase>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = false,
                        durationMillis = 6,
                        time = timestamp,
                        stackTrace = "stacktrace"
                )
        ), parsed)
    }

    @Test
    fun canParseErroredTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\">\n            <error />\n        </testcase>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = false,
                        durationMillis = 6,
                        time = timestamp
                )
        ), parsed)
    }

    @Test
    fun ignoresSkippedTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\">\n            <skipped />\n        </testcase>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(emptyList<ResultDto>(), parsed)
    }

    @Test
    fun canParseMultipleTestSuites() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\" />\n    </testsuite>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass2\" name=\"myTest\" time=\"0.01\" />\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = true,
                        durationMillis = 6,
                        time = timestamp
                ),
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass2#myTest",
                        retryNum = 0,
                        passed = true,
                        durationMillis = 10,
                        time = timestamp
                )
        ), parsed)
    }

    @Test
    fun canParseEmptyTestSuite() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\" />\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(emptyList<ResultDto>(), parsed)
    }

    @Test
    fun canParsNoTestSuites() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites />"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(emptyList<ResultDto>(), parsed)
    }

}