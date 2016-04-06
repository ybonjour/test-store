package ch.yvu.teststore.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

class TestStorePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('teststore', TestStorePluginExtension)
        project.task(storeResultsTaskSettings(), 'storeResults') << {

            System.out.println(project.buildDir)
        }
    }

    private static Map<String, String> storeResultsTaskSettings() {
        def settings = [:]
        settings.put(Task.TASK_DESCRIPTION, 'Stores test results in test-store')
        return settings
    }
}