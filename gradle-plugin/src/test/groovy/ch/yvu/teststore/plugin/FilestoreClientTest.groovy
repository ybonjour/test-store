package ch.yvu.teststore.plugin

import groovy.mock.interceptor.MockFor
import org.joda.time.DateTime
import org.junit.Test

import static java.util.UUID.randomUUID

class FilestoreClientTest {
    private static final UUID TEST_SUITE = randomUUID()
    private static final DateTime NOW = new DateTime()

    @Test
    public void canCreateARun() {
        String revision = "abc123"
        UUID runId = randomUUID()
        def mock = new MockFor(HttpClient)
        mock.demand.postForm { String path, Map<String, String> parameters ->
            assert path == "/testsuites/$TEST_SUITE/runs"
            assert parameters == [revision: revision, time: NOW]
            return runId
        }

        mock.use {
            def client = new FilestoreClient(httpClient: new HttpClient("", 0), testSuiteId: TEST_SUITE)
            def result = client.createRun(revision, NOW)
            assert result == runId
        }
    }
}
