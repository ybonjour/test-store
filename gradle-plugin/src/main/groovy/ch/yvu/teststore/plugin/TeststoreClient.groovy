package ch.yvu.teststore.plugin

import java.text.SimpleDateFormat

class TeststoreClient {
    HttpClient httpClient
    UUID testSuiteId

    private static final String ISO_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

    def createRun(String revision, Date time) {
        def timeString = new SimpleDateFormat(ISO_DATE_FORMAT).format(time);
        def params = [revision: revision, time: timeString]
        def response = httpClient.postForm("/testsuites/${testSuiteId.toString()}/runs", params)
        return UUID.fromString(response.id)
    }
}
