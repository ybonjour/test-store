package ch.yvu.teststore.idea.plugin.dto;

import java.util.List;

public class PageDto<T> {

	private List<T> results;

	public List<T> getResults() {
		return results;
	}

	public void setResults(List<T> results) {
		this.results = results;
	}
}
