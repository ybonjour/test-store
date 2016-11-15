package ch.yvu.teststore.plugin

import org.gradle.api.tasks.testing.*

import static org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED
import static org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS

class TestStoreTestListener implements TestListener {
    private final TestStoreTestOutputListener outputListener
    private final TestStorePluginExtension pluginExtension
    private final TeststoreClientFactory factory

    private UUID runId

    public TestStoreTestListener(TestStorePluginExtension pluginExtension, TeststoreClientFactory factory, TestStoreTestOutputListener outputListener) {
        this.factory = factory
        this.pluginExtension = pluginExtension
        this.outputListener = outputListener
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
            runId = factory.createClient().createRun(pluginExtension.revision, new Date());
        }
    }

    @Override
    void afterTest(TestDescriptor testDescriptor, TestResult result) {
        if (!pluginExtension.incremental) return
        if (result.resultType == SKIPPED) return

        def passed = result.resultType == SUCCESS
        def stackTrace = result.exception != null ? stackTrace(result.exception) : ""
        def log = outputListener.getLogOutput(testDescriptor)
        factory.createClient().insertTestResult(
                runId,
                testDescriptor.className + "#" + testDescriptor.name,
                passed,
                result.endTime - result.startTime,
                new Date(),
                stackTrace,
                log
        )
    }

    private static String stackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }
}
