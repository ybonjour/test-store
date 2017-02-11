package ch.yvu.teststore.idea.plugin.dto;

import java.util.List;

public class ResultDto {

	private String testName;
	private String testResult;
	private List<ResultDetailDto> results;

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestResult() {
		return testResult;
	}

	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}

	public List<ResultDetailDto> getResults() {
		return results;
	}

	public void setResults(List<ResultDetailDto> results) {
		this.results = results;
	}
}
