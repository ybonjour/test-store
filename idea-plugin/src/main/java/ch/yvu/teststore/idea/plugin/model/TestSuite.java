package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadRuns;
import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;

public class TestSuite implements Model {

	private final String name;
	private final String id;

	public TestSuite(String name, String id) {
		this.name = name;
		this.id = id;
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getText() {
		return name;
	}

	@Override
	public LoadTask loadChildrenTask() {
		return new LoadRuns(id);
	}

	@Override
	public Runnable doubleClickAction() {
		return null;
	}
}
