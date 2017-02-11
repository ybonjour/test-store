package ch.yvu.teststore.idea.plugin.model;

import ch.yvu.teststore.idea.plugin.load.LoadResults;
import ch.yvu.teststore.idea.plugin.load.LoadTask;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.Map;

public class Run implements Model {

	private static Map<String, Icon> ICONS = new HashMap<>();

	static {
		ICONS.put("UNKNOWN", AllIcons.RunConfigurations.Unknown);
		ICONS.put("PASSED", AllIcons.RunConfigurations.TestPassed);
		ICONS.put("FAILED", AllIcons.RunConfigurations.TestFailed);
		ICONS.put("PASSED_WITH_RETRIES", AllIcons.RunConfigurations.TestTerminated);
	}

	private final String id;
	private final String revision;
	private final LocalDateTime time;
	private final String result;

	public Run(String id, String revision, LocalDateTime time, String result) {
		this.id = id;
		this.revision = revision;
		this.time = time;
		this.result = result;
	}

	@Override
	public Icon getIcon() {
		return ICONS.get(result);
	}

	@Override
	public String getText() {
		if (time == null)
			return "-";
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT, FormatStyle.MEDIUM);
		String text = time.format(formatter);
		if (revision != null) {
			text = text + " (" + revision + ")";
		} return text;
	}

	@Override
	public LoadTask loadChildrenTask() {
		return new LoadResults(id);
	}

	@Override
	public Runnable doubleClickAction() {
		return null;
	}
}
