package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.WindowFactory;
import ch.yvu.teststore.idea.plugin.action.JumpToTest;
import ch.yvu.teststore.idea.plugin.action.SaveFailureReason;
import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class Result extends Model {

	private final String run;
	private final String result;
	private final String testName;
	private final String stackTrace;
	private final String log;
	private String failureReason;

	private final static String[] FAILURE_REASONS = new String[] { "Application Bug", "Test Bug", "Flaky" };

	private final static Map<String, Icon> ICONS = new HashMap<>();

	static {
		ICONS.put("UNKNOWN", AllIcons.RunConfigurations.Unknown);
		ICONS.put("PASSED", AllIcons.RunConfigurations.TestPassed);
		ICONS.put("FAILED", AllIcons.RunConfigurations.TestFailed);
		ICONS.put("RETRIED", AllIcons.RunConfigurations.TestTerminated);
	}

	public Result(String testName, String result, String stackTrace, String log, String failureReason, String run, String baseUrl) {
		super(baseUrl);
		this.result = result;
		this.testName = testName;
		this.stackTrace = stackTrace;
		this.log = log;
		this.failureReason = failureReason;
		this.run = run;
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
		if (testName.length() <= 50)
			return testName;

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
			JPopupMenu menu = new JPopupMenu();
			addStackTrace(menu);
			addLog(menu);
			addFailureReasons(menu);
			menu.show(e.getComponent(), e.getX(), e.getY());
		};
	}

	private void addStackTrace(JPopupMenu menu) {
		if (stackTrace == null || stackTrace.equals(""))
			return;

		JMenuItem item = new JMenuItem("Show Stacktrace...");
		item.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				TextDialog dialog = new TextDialog(WindowFactory.currentProject, "Stacktrace", stackTrace);
				dialog.show();
			}
		});

		menu.add(item);
	}

	private void addLog(JPopupMenu menu) {
		if (log == null || log.equals(""))
			return;

		JMenuItem item = new JMenuItem("Show Log...");
		item.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				TextDialog dialog = new TextDialog(WindowFactory.currentProject, "Log", log);
				dialog.show();
			}
		});
		menu.add(item);
	}

	private void addFailureReasons(JPopupMenu menu) {
		if (!"FAILED".equals(result))
			return;

		menu.addSeparator();
		menu.add(new JLabel("Failure Reason:", AllIcons.General.Mdot_empty, SwingConstants.LEADING));

		for (String failureReason : FAILURE_REASONS) {
			Icon icon = failureReason.equals(Result.this.failureReason) ? AllIcons.Actions.Checked :AllIcons.General.Mdot_empty;

			JMenuItem item = new JMenuItem(failureReason, icon);
			item.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					super.mousePressed(e);
					Result.this.failureReason = failureReason;
					ProgressManager.getInstance()
							.run(new SaveFailureReason(WindowFactory.currentProject,
									Result.this.run,
									Result.this.testName,
									failureReason,
									getBaseUrl()));

				}
			});
			menu.add(item);
		}
	}

	private String getClassName() {
		return testName.split("#")[0];
	}

	private String getMethodName() {
		return testName.split("#")[1];
	}

	private class TextDialog extends DialogWrapper {

		private final String text;

		TextDialog(@Nullable Project project, String title, String text) {
			super(project, false);
			this.text = text;

			init();
			setTitle(title);
		}

		@Nullable
		@Override
		protected JComponent createCenterPanel() {
			return new JScrollPane(new JTextArea(text));
		}

		@NotNull
		@Override
		protected Action[] createActions() {
			return new Action[0];
		}
	}
}
