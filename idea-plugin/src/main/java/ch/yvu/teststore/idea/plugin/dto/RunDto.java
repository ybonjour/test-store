package ch.yvu.teststore.idea.plugin.dto;

import java.time.LocalDateTime;

public class RunDto {
	private String id;
	private String revision;
	private long time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
