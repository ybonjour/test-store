package ch.yvu.teststore.plugin

import org.gradle.api.tasks.testing.*

class TestStoreTestOutputListener implements TestOutputListener {
    private Map<String, String> logOutputs = new HashMap<>()

    @Override
    void onOutput(TestDescriptor testDescriptor, TestOutputEvent outputEvent) {1
        def testName = testName(testDescriptor)
        logOutputs.put(testName, logOutputs.get(testName, "") + outputEvent.message)
    }

    String getLogOutput(TestDescriptor testDescriptor) {
        def log = logOutputs.get(testName(testDescriptor), "")
        logOutputs.remove(testName(testDescriptor))
        return log
    }

    private static String testName(TestDescriptor testDescriptor) {
        return testDescriptor.className + "#" + testDescriptor.name;
    }
}
