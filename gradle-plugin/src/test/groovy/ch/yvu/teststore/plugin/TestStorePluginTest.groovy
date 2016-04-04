package ch.yvu.teststore.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.junit.Test

import static org.gradle.testfixtures.ProjectBuilder.builder
import static org.junit.Assert.assertTrue

class TestStorePluginTest {
    @Test
    public void addsStoreResultsTaskToProject() {
        Project project = builder().build();
        project.pluginManager.apply 'ch.yvu.teststore'

        assertTrue(project.tasks.storeResults instanceof DefaultTask)
    }
}
