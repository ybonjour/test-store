package ch.yvu.teststore.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class TestStorePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('teststore', TestStorePluginExtension)
        project.task(storeResultsTaskSettings(), 'storeResults') << {
            def testSuiteId = UUID.fromString(project.teststore.testSuite)
            def client = createClient(project.teststore.host, project.teststore.port, testSuiteId)
            client.createRun(project.teststore.revision, new Date())
        }
    }

    private static createClient(String host, int port, UUID testSuiteId) {
        def httpClient = new HttpClient(host, port)
        return new TeststoreClient(httpClient: httpClient, testSuiteId: testSuiteId)
    }


    private static Map<String, String> storeResultsTaskSettings() {
        def settings = [:]
        settings.put(Task.TASK_DESCRIPTION, 'Stores test results in test-store')
        return settings
    }
}