package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Loading extends Model {

	public Loading() {
		super(null);
	}

	@Override
	public Icon getIcon() {
		return AllIcons.RunConfigurations.TestInProgress1;
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

	@Override
	public Runnable rightClickAction(MouseEvent e) {
		return null;
	}
}
