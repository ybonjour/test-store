package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class Error extends Model<Error> {

	public Error() {
		super(null);
	}

	@Override
	public Icon getIcon() {
		return AllIcons.RunConfigurations.ConfigurationWarning;
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
