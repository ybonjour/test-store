package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadRuns;
import ch.yvu.teststore.idea.plugin.load.LoadTask;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class TestSuite extends Model {

	private final String name;
	private final String id;

	public TestSuite(String name, String id, String baseUrl) {
		super(baseUrl);
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
		return new LoadRuns(id, getBaseUrl());
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
