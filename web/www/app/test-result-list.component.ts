import {Component} from 'angular2/core';
import {OnInit} from 'angular2/core';
import { RouteParams } from 'angular2/router';
import {TestResultService} from './test-result.service'
import {TestWithResults} from './test-with-results'

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
	expandedStacktraces: String[] = [];

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
			this.passedResults = results["PASSED"];
		}

		if(results["FAILED"] != null) {
			this.failedResults = results["FAILED"];
		}

		if(results["RETRIED"] != null) {
			this.retriedResults = results["RETRIED"];
		}
	}

	isExpanded(testName: String, retryNum: number) {
		var key = this.stackTraceIdentifier(testName, retryNum);
		var result = this.expandedStacktraces.indexOf(key);
		return result >= 0;
	}

	toggleExpanded(testName: String, retryNum: number) {
		var key = this.stackTraceIdentifier(testName, retryNum);
		var index = this.expandedStacktraces.indexOf(key);
		if(index < 0) {
			this.expandedStacktraces.push(key);
		} else {
			delete this.expandedStacktraces[index]
		}
	}

	private stackTraceIdentifier(testName: String, retryNum: number) {
		return testName + "$" + retryNum;
	}
}