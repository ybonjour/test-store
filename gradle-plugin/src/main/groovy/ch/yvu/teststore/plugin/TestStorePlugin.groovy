package ch.yvu.teststore.plugin

import org.gradle.api.*

class TestStorePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('teststore', TestStorePluginExtension)
        project.task(storeResultsTaskSettings(), 'storeResults') << {
            def client = createClient(project.teststore)
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

        project.gradle.addListener(new TestStoreTestListener(project.teststore, new TeststoreClientFactory(project.teststore)))
    }

    private static createClient(TestStorePluginExtension extension) {
        def factory = new TeststoreClientFactory(extension)
        return factory.createClient()
    }

    private static Map<String, String> storeResultsTaskSettings() {
        def settings = [:]
        settings.put(Task.TASK_DESCRIPTION, 'Stores test results in test-store')
        return settings
    }
}