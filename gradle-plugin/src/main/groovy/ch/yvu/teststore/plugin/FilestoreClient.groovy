package ch.yvu.teststore.plugin

import org.joda.time.DateTime

class FilestoreClient {
    HttpClient httpClient
    UUID testSuiteId

    def createRun(String revision, DateTime time) {
        def params = [revision: revision, time: time]
        httpClient.postForm("/testsuites/${testSuiteId.toString()}/runs", params)
    }
}
