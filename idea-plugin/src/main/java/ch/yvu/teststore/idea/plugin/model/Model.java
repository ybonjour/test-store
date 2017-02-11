package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadTask;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.List;

public interface Model<T extends Model> {

	Icon getIcon();

	String getText();

	LoadTask<T> loadChildrenTask();

	Runnable doubleClickAction();

	Runnable rightClickAction(MouseEvent e);
}
