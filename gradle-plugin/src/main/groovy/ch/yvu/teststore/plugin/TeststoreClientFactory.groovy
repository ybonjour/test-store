package ch.yvu.teststore.plugin

class TeststoreClientFactory {

    private final TestStorePluginExtension extension

    def TeststoreClientFactory(TestStorePluginExtension extension) {
        this.extension = extension
    }

    def createClient() {
        def httpClient = new HttpClient(extension.host, extension.port)
        def testSuiteId = UUID.fromString(extension.testSuite)
        return new TeststoreClient(httpClient: httpClient, testSuiteId: testSuiteId)
    }
}
