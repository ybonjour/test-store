package ch.yvu.teststore.idea.plugin.dto;

public class TestSuiteOverviewDto {
	private TestSuiteDto testSuite;
	private String lastRunResult;

	public TestSuiteDto getTestSuite() {
		return testSuite;
	}

	public void setTestSuite(TestSuiteDto testSuite) {
		this.testSuite = testSuite;
	}

	public String getLastRunResult() {
		return lastRunResult;
	}

	public void setLastRunResult(String lastRunResult) {
		this.lastRunResult = lastRunResult;
	}
}
