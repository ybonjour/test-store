package ch.yvu.teststore.idea.plugin.load;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.dto.TestSuiteDto;
import ch.yvu.teststore.idea.plugin.model.TestSuite;
import com.google.gson.Gson;

import java.util.List;

public class LoadTestSuites extends LoadTask<TestSuite> {

	public LoadTestSuites(String baseUrl) {
		super("Load Test Suites", baseUrl);
	}

	@Override
	public List<TestSuite> fetch() {
		String json = getJson("testsuites/raw");
		Gson gson = new Gson();
		TestSuiteDto[] testSuites = gson.fromJson(json, TestSuiteDto[].class);

		return stream(testSuites) //
				.map((TestSuiteDto dto) -> new TestSuite(dto.getName(), dto.getId(), getBaseUrl())) //
				.collect(toList());
	}
}
