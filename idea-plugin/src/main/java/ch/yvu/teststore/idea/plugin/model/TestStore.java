package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import ch.yvu.teststore.idea.plugin.load.LoadTestSuites;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class TestStore extends Model {


	public TestStore(String baseUrl) {
		super(baseUrl);
	}

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
		return new LoadTestSuites(getBaseUrl());
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
