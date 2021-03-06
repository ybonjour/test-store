package ch.yvu.teststore.plugin

import groovy.json.JsonSlurper
import groovy.mock.interceptor.MockFor
import org.junit.Test

import java.text.SimpleDateFormat

import static java.util.UUID.randomUUID

class TeststoreClientTest {
    private static final UUID TEST_SUITE = randomUUID()
    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
    private static final Date NOW = new Date()

    @Test
    public void canCreateARun() {
        String revision = "abc123"
        def time = NOW
        UUID runId = randomUUID()
        def response = [id:runId.toString()]
        def mock = new MockFor(HttpClient)
        mock.demand.postJson { String path, String json ->
            assert path == "/testsuites/$TEST_SUITE/runs"

            def slurper = new JsonSlurper();
            def run = slurper.parse(json.bytes)
            assert revision == run.revision
            assert new SimpleDateFormat(ISO_DATE_FORMAT).format(time) == run.time

            return response
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            def result = client.createRun(revision, time)
            assert result == runId
        }
    }

    @Test
    public void canInsertAnXMLResult() {
        def runId = randomUUID()
        def mock = new MockFor(HttpClient)
        def junitXml = "<someXML />"
        mock.demand.postXml { String path, String xml ->
            assert path == "/runs/${runId.toString()}/results"
            assert junitXml == xml
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            client.insertTestResult(runId, junitXml)
        }

    }

    @Test
    public void canInsertAResult() {
        def runId = randomUUID()
        def testName = "myPackage#MyTest"
        def passed = false
        def duration = 1234L
        def time = NOW
        def stackTracke = "Some stacktrace"
        def log = "Log message"
        def mock = new MockFor(HttpClient)

        mock.demand.postJson { String path, String json ->
            assert path == "/runs/$runId/results"

            def slurper = new JsonSlurper();
            def result = slurper.parse(json.bytes)

            assert runId.toString() == result.run
            assert testName == result.testName
            assert 0 == result.retryNum
            assert passed == result.passed
            assert duration == result.durationMillis
            assert new SimpleDateFormat(ISO_DATE_FORMAT).format(time) == result.time
            assert stackTracke == result.stackTrace
            assert log == result.log
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            client.insertTestResult(runId, testName, passed, duration, time, stackTracke, log)
        }
    }\

    @Test
    public void canInsertARevisionWithoutUrl() {
        def runId = randomUUID()
        def scmChange = new ScmChanges.ScmChange(
                revision: "abcd",
                author: "Yves Bonjour",
                description: "Changes the world",
                time: NOW
        )

        def mock = new MockFor(HttpClient)

        mock.demand.postJson { String path, String json ->

            assert path == "/runs/$runId/revisions"

            def slurper = new JsonSlurper()
            def result = slurper.parse(json.bytes)

            assert scmChange.getRevision() == result.revision
            assert new SimpleDateFormat(ISO_DATE_FORMAT).format(scmChange.time) == result.time
            assert scmChange.author == result.author
            assert scmChange.description == result.comment
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            client.insertScmChange(runId, scmChange, null)
        }
    }

    @Test
    public void canInsertARevisionWithUrl() {
        def runId = randomUUID()
        def scmChange = new ScmChanges.ScmChange(
                revision: "abcd",
                time: NOW
        )

        def mock = new MockFor(HttpClient)

        mock.demand.postJson { String path, String json ->

            assert path == "/runs/$runId/revisions"

            def slurper = new JsonSlurper()
            def result = slurper.parse(json.bytes)

            assert "http://myUrl.com/abcd" == result.url
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            client.insertScmChange(runId, scmChange, 'http://myUrl.com/${revision}')
        }
    }

}
