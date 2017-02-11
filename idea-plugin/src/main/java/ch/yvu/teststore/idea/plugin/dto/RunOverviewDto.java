package ch.yvu.teststore.idea.plugin.dto;

public class RunOverviewDto {

	private RunDto run;
	private RunStatisticsDto runStatistics;

	public RunDto getRun() {
		return run;
	}

	public void setRun(RunDto run) {
		this.run = run;
	}

	public RunStatisticsDto getRunStatistics() {
		return runStatistics;
	}

	public void setRunStatistics(RunStatisticsDto runStatistics) {
		this.runStatistics = runStatistics;
	}
}
