package ch.yvu.teststore.idea.plugin.load;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.dto.TestSuiteOverviewDto;
import ch.yvu.teststore.idea.plugin.model.TestSuite;
import com.google.gson.Gson;

import java.util.List;

public class LoadTestSuites extends LoadTask<TestSuite> {

	public LoadTestSuites(String baseUrl) {
		super("Load Test Suites", baseUrl);
	}

	@Override
	public List<TestSuite> fetch() {
		String json = getJson("/testsuites");
		Gson gson = new Gson();
		TestSuiteOverviewDto[] testSuites = gson.fromJson(json, TestSuiteOverviewDto[].class);

		return stream(testSuites) //
				.map((TestSuiteOverviewDto dto) -> new TestSuite(dto.getTestSuite().getName(), dto.getTestSuite().getId(), getBaseUrl())) //
				.collect(toList());
	}
}
