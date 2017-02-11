package ch.yvu.teststore.idea.plugin.load;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import ch.yvu.teststore.idea.plugin.dto.ResultDto;
import ch.yvu.teststore.idea.plugin.model.Result;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class LoadResults extends LoadTask<Result> {

	private static List<String> RESULT_ORDER = Arrays.asList("FAILED", "RETRIED", "PASSED", "UNKNOWN");

	private final String runId;

	public LoadResults(String runId) {
		super("Loading results for run " + runId);
		this.runId = runId;
	}

	@Override
	public List<Result> fetch() {
		String json = getJson("runs/" + runId + "/results");

		Gson gson = new Gson();
		ResultDto[] resultDtos = gson.fromJson(json, ResultDto[].class);

		List<Result> results = stream(resultDtos).map((ResultDto dto) -> new Result(
				dto.getTestName(),
				dto.getTestResult(),
				dto.getResults().get(0).getStackTrace())).collect(toList());
		results.sort((r1, r2) -> {
			int priorityR1 = RESULT_ORDER.indexOf(r1.getResult());
			int priorityR2 = RESULT_ORDER.indexOf(r2.getResult());
			int difference = priorityR1 - priorityR2;

			if (difference != 0)
				return difference;

			return r1.getText().compareTo(r2.getText());
		});

		return results;
	}
}
