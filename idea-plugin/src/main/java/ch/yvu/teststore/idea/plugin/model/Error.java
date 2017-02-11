package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Error implements Model<Error> {

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public String getText() {
		return "Error";
	}

	@Override
	public LoadTask<Error> loadChildrenTask() {
		return null;
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
