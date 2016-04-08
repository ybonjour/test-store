package ch.yvu.teststore.plugin

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
        UUID runId = randomUUID()
        def response = [id:runId.toString()]
        def mock = new MockFor(HttpClient)
        mock.demand.postForm { String path, Map<String, String> parameters ->
            assert path == "/testsuites/$TEST_SUITE/runs"
            assert parameters == [revision: revision, time: new SimpleDateFormat(ISO_DATE_FORMAT).format(NOW)]
            return response
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            def result = client.createRun(revision, NOW)
            assert result == runId
        }
    }

    @Test
    public void canInsertAResult() {
        def runId = randomUUID()
        def mock = new MockFor(HttpClient)
        def junitXml = "<someXML />"
        mock.demand.postXml { String path, String xml ->
            assert path == "/testsuites/$TEST_SUITE/runs/${runId.toString()}/results"
            assert junitXml == xml
        }

        mock.use {
            def client = new TeststoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            client.insertTestResult(runId, junitXml)
        }

    }
}
