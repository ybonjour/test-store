package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;

public class Loading implements Model {

	@Override
	public Icon getIcon() {
		return AllIcons.Icons.Ide.SpeedSearchPrompt;
	}

	@Override
	public String getText() {
		return "Loading...";
	}

	@Override
	public LoadTask loadChildrenTask() {
		return null;
	}

	@Override
	public Runnable doubleClickAction() {
		return null;
	}
}
