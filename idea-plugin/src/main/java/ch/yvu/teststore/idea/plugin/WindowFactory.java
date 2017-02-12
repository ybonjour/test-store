package ch.yvu.teststore.idea.plugin;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class WindowFactory implements ToolWindowFactory {

	//TODO: clean up
	public static Project currentProject;

	@Override
	public void createToolWindowContent(
			@NotNull Project project, @NotNull ToolWindow toolWindow) {

		currentProject = project;

		Main main = new Main();
		ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
		Content content = contentFactory.createContent(main.getToolWindow(), "", false);

		toolWindow.getContentManager().addContent(content);
	}
}
