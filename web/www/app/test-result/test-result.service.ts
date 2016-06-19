import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import {TestResult} from "./test-result";
import {TestWithResults} from "./test-with-results";

@Injectable()
export class TestResultService {
	constructor(private _http: Http) {}

	getResultsGrouped(runId: String): Observable<Map<String, TestWithResults[]>> {
		return this._http.get("/api/runs/" + runId + "/results/grouped")
			.map(TestResultService.extractBody)
			.catch(TestResultService.extractError)
	}

	private static extractBody(response: Response) {
		if(response.status != 200) throw new Error("Bad response status: " + response.status);

		return TestResultService.convertGroupedResults(response.json());
	}

	private static extractError (error: any) {
		let errorMessage = error.message || "Server error";
		return Observable.throw(errorMessage);
	}

	private static convertGroupedResults(json: any) {
		var result = {};
		for(var key in json) {
			if(!json.hasOwnProperty(key)) continue;
			result[key] = TestResultService.convertTestsWithResultsToJson(json[key]);
		}
		return result;
	}

	private static convertTestsWithResultsToJson(json: any): TestWithResults[] {
		var testsWithResults = [];
		for(var testResultJson of json) {
			var results = [];
			for(var resultJson of testResultJson.results) {
				var result = new TestResult();
				result.run = resultJson.run;
				result.testName = resultJson.testName;
				result.retryNum = resultJson.retryNum;
				result.passed = resultJson.passed;
				result.durationMillis = resultJson.durationMillis;
				result.stackTrace = resultJson.stackTrace;
				results.push(result);
			}

			results.sort(function(result1, result2) {return result2.retryNum - result1.retryNum;});

			var testWithResults = new TestWithResults();
			testWithResults.testName = testResultJson.testName;
			testWithResults.testResult = testResultJson.testResult;
			testWithResults.results = results;

			testsWithResults.push(testWithResults);
		}

		return testsWithResults;
	}
}