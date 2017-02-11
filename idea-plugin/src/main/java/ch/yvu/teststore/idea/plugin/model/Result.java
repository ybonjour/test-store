package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.WindowFactory;
import ch.yvu.teststore.idea.plugin.action.JumpToTest;
import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class Result implements Model {

	private final String result;
	private final String testName;

	private final static Map<String, Icon> ICONS = new HashMap<>();

	static {
		ICONS.put("UNKNOWN", AllIcons.RunConfigurations.Unknown);
		ICONS.put("PASSED", AllIcons.RunConfigurations.TestPassed);
		ICONS.put("FAILED", AllIcons.RunConfigurations.TestFailed);
		ICONS.put("RETRIED", AllIcons.RunConfigurations.TestTerminated);
	}

	public Result(String testName, String result) {
		this.result = result;
		this.testName = testName;
	}

	public String getResult() {
		return result;
	}

	@Override
	public String toString() {
		return testName;
	}

	@Override
	public Icon getIcon() {
		return ICONS.get(result);
	}

	@Override
	public String getText() {
		if(testName.length() <= 50) return testName;

		String firstPart = safeSubstring(testName, 0, 17);
		String secondPart = safeSubstring(testName, testName.length() - 30, testName.length());

		return firstPart + "..." + secondPart;
	}

	private static String safeSubstring(String s, int low, int high) {
		return s.substring(Math.max(0, low), Math.min(high, s.length()));
	}

	@Override
	public LoadTask loadChildrenTask() {
		return null;
	}

	@Override
	public Runnable doubleClickAction() {
		return new JumpToTest(WindowFactory.currentProject, getClassName(), getMethodName());
	}

	@Override
	public Runnable rightClickAction(MouseEvent e) {
		return () -> {
			JPopupMenu popup = new JPopupMenu();
			popup.add(new JMenuItem("Show Details..."));
			popup.show(e.getComponent(), e.getX(), e.getY());
		};
	}

	private String getClassName() {
		return testName.split("#")[0];
	}

	private String getMethodName() {
		return testName.split("#")[1];
	}
}
