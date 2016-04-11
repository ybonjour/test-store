package ch.yvu.teststore.insert

import ch.yvu.teststore.insert.dto.ResultDto
import org.junit.Assert.assertEquals
import org.junit.Test

class JunitXmlParserTest {

    @Test
    fun canParsePassedTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\"/>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = true,
                        durationMillis = 6
                )
        ), parsed)
    }

    @Test
    fun canParseFailedTest() {
        val xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<testsuites>\n    <testsuite errors=\"0\" skipped=\"0\" tests=\"1\" failures=\"0\" timestamp=\"2016-03-31T17:51:02\">\n        <testcase classname=\"ch.yvu.teststore.MyTestClass\" name=\"myTest\" time=\"0.006\">\n            <failure />\n        </testcase>\n    </testsuite>\n</testsuites>"

        val parsed = JunitXMLParser.parse(xml)

        assertEquals(listOf(
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass#myTest",
                        retryNum = 0,
                        passed = false,
                        durationMillis = 6
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
                        durationMillis = 6
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
                        durationMillis = 6
                ),
                ResultDto(
                        testName = "ch.yvu.teststore.MyTestClass2#myTest",
                        retryNum = 0,
                        passed = true,
                        durationMillis = 10
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