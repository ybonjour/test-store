package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import ch.yvu.teststore.idea.plugin.load.LoadTestSuites;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class TestStore implements Model {

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getText() {
		return "Test Suites";
	}

	@Override
	public LoadTask loadChildrenTask() {
		return new LoadTestSuites();
	}

	@Override
	public Runnable doubleClickAction() {
		return null;
	}

	@Override
	public Runnable rightClickAction(MouseEvent e) {
		return null;
	}
}
