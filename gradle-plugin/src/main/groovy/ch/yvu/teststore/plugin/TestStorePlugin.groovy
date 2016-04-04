package ch.yvu.teststore.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestStorePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.task('storeResults') << {
        }
    }
}
