import {Component} from 'angular2/core';
import {OnInit} from 'angular2/core';
import { RouteParams } from 'angular2/router';
import {TestResultService} from './test-result.service'
import {TestWithResults} from './test-with-results'
import {retry} from "../jspm_packages/npm/rxjs@5.0.0-beta.6/operator/retry";
import {TestResult} from "./test-result";

@Component({
	templateUrl: 'app/test-result-list.html',
	styleUrls: ['app/test-result-list.css']
})
export class TestResultListComponent implements OnInit {
	errorMessage: string;
	runId: string;
	passedResults: TestWithResults[] = [];
	failedResults: TestWithResults[] = [];
	retriedResults: TestWithResults[] = [];

	constructor(
		private _testResultService: TestResultService,
		private _routeParams: RouteParams) {
	}

	ngOnInit() {
		this.runId = this._routeParams.get('run_id');
		this.getResults(this.runId);
	}

	getResults(runId: String) {
		this._testResultService.getResultsGrouped(runId)
					.subscribe(
						results => this.extractResults(results),
						error => this.errorMessage = <any>error);
	}

	extractResults(results: Map<String, TestWithResults[]>) {
		if(results["PASSED"] != null) {
			this.passedResults = this.convertJson(results["PASSED"]);
		}

		if(results["FAILED"] != null) {
			this.failedResults = this.convertJson(results["FAILED"]);
		}

		if(results["RETRIED"] != null) {
			this.retriedResults = this.convertJson(results["RETRIED"]);
		}
	}

	private convertJson(json: any): TestWithResults[] {
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