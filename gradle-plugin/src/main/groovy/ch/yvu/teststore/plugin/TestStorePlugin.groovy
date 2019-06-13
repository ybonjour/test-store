package ch.yvu.teststore.plugin

import org.gradle.api.*
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging

class TestStorePlugin implements Plugin<Project> {

    private final Logger logger = Logging.getLogger("TestStorePluginLogger")

    @Override
    void apply(Project project) {
        project.extensions.create('teststore', TestStorePluginExtension)
        project.task(storeResultsTaskSettings(), 'storeResults') doLast {
            def client = createClient(project.teststore)
            def runId = client.createRun(project.teststore.revision, new Date())
            logger.info("Created run $runId")
            def fileWalker = new FileWalker(baseDir: project.rootDir, pattern: project.teststore.xmlReports)
            fileWalker.walkFileContents { filePath, xml ->
                try {
                    client.insertTestResult(runId, xml)
                    logger.info("Stored $filePath")
                } catch(Exception e) {
                    logger.warn("Could not store $filePath (${e.getMessage()}")
                }
            }
        }

        TestStoreTestOutputListener testOutputListener = new TestStoreTestOutputListener()
        project.gradle.addListener(testOutputListener)
        project.gradle.addListener(new TestStoreTestListener(project.teststore, new TeststoreClientFactory(project.teststore), testOutputListener, new ScmChangesFactory(project.teststore)))
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