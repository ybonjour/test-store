package ch.yvu.teststore.plugin

import org.gradle.api.tasks.testing.*

import static org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED
import static org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS

class TestStoreTestListener implements TestListener {
    private final TestStoreTestOutputListener outputListener
    private final TestStorePluginExtension pluginExtension
    private final TeststoreClientFactory clientFactory
    private final ScmChangesFactory scmChangesFactory

    private UUID runId

    public TestStoreTestListener(TestStorePluginExtension pluginExtension, TeststoreClientFactory clientFactory, TestStoreTestOutputListener outputListener, ScmChangesFactory scmChangesFactory) {
        this.clientFactory = clientFactory
        this.pluginExtension = pluginExtension
        this.outputListener = outputListener
        this.scmChangesFactory = scmChangesFactory
    }

    @Override
    void beforeSuite(TestDescriptor suite) {


    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {

    }

    @Override
    void beforeTest(TestDescriptor testDescriptor) {
        if (!pluginExtension.incremental) return

        if (runId == null) {
            TeststoreClient client = clientFactory.createClient();
            runId = client.createRun(pluginExtension.revision, new Date());
            ScmChanges scmChanges = scmChangesFactory.createScmChanges();
            for(ScmChanges.ScmChange scmChange : scmChanges.getChanges()) {
                client.insertScmChange(runId, scmChange)
            }
        }
    }

    @Override
    void afterTest(TestDescriptor testDescriptor, TestResult result) {
        if (!pluginExtension.incremental) return
        if (result.resultType == SKIPPED) return

        def passed = result.resultType == SUCCESS
        def stackTrace = result.exception != null ? stackTrace(result.exception) : ""
        def log = passed ? "" : outputListener.getLogOutput(testDescriptor)
        def client = clientFactory.createClient()
        try {
            client.insertTestResult(
                    runId,
                    testDescriptor.className + "#" + testDescriptor.name,
                    passed,
                    result.endTime - result.startTime,
                    new Date(),
                    stackTrace,
                    log
            )
        } catch(Exception e) {
            System.err.println("Result could not be sent to test-store")
        }
    }

    private static String stackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
