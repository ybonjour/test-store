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
            def runId = client.createRun(project.teststore.revision, new Date())
            System.out.println("Created run $runId")
            def fileWalker = new FileWalker(baseDir: project.rootDir, pattern: project.teststore.xmlReports)
            fileWalker.walkFileContents { filePath, xml ->
                try {
                    client.insertTestResult(runId, xml)
                    System.out.println("Stored $filePath")
                } catch(Exception e) {
                    System.err.println("Could not store $filePath (${e.getMessage()}")
                }
            }
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