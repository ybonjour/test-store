package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;

import javax.swing.*;
import java.awt.event.MouseEvent;

public abstract class Model<T extends Model> {

	private final String baseUrl;

	public Model(String baseUrl) {

		this.baseUrl = baseUrl;
	}

	protected String getBaseUrl() {
		return baseUrl;
	}

	public abstract Icon getIcon();

	public abstract String getText();

	public abstract LoadTask<T> loadChildrenTask();

	public abstract Runnable doubleClickAction();

	public abstract Runnable rightClickAction(MouseEvent e);
}
