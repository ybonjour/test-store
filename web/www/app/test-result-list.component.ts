import {Component} from 'angular2/core';
import {OnInit} from 'angular2/core';
import { RouteParams } from 'angular2/router';
import {TestResultService} from './test-result.service'
import {TestResult} from './test-result'

@Component({
  templateUrl: 'app/test-result-list.html' 
})
export class TestResultListComponent implements OnInit {
	errorMessage: string;
	results: TestResult[];
	runId: String;
	
	constructor(
		private _testResultService: TestResultService,
		private _routeParams: RouteParams) {}

	ngOnInit() {
		this.runId = this._routeParams.get('run_id');
		this.getResults(this.runId);
	}

	getResults(runId: String) {
		this._testResultService.getResults(runId)
					.subscribe(
						results => this.results = results,
						error => this.errorMessage = <any>error);
	}
}