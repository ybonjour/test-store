package ch.yvu.teststore.idea.plugin.load;

import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.dto.PageDto;
import ch.yvu.teststore.idea.plugin.dto.RunOverviewDto;
import ch.yvu.teststore.idea.plugin.model.Run;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class LoadRuns extends LoadTask<Run> {

	private final String testSuiteId;

	public LoadRuns(String testSuiteId) {
		super("Load runs for test suite " + testSuiteId);
		this.testSuiteId = testSuiteId;
	}

	@Override
	public List<Run> fetch() {
		String json = getJson("/testsuites/" + testSuiteId + "/runs/overview?fetchSize=10");
		Gson gson = new Gson();
		PageDto<RunOverviewDto> runOverviewPage = gson.fromJson(json, new TypeToken<PageDto<RunOverviewDto>>() {

		}.getType());

		return runOverviewPage.getResults()
				.stream()
				.map((RunOverviewDto dto) -> new Run(dto.getRun().getId(), dto.getRun().getRevision(),
						LocalDateTime.ofInstant(Instant.ofEpochMilli(dto.getRun().getTime()), ZoneId.systemDefault()), dto.getRunStatistics().getResult()))
				.collect(toList());
	}
}
