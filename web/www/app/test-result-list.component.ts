import {Component} from 'angular2/core';
import {Injectable} from 'angular2/core';
import {OnInit} from 'angular2/core';
import { RouteParams } from 'angular2/router';
import {TestResultService} from './test-result.service'
import {TestResult} from './test-result'

@Component({
  templateUrl: 'app/test-result-list.html' 
})
export class TestResultListComponent implements OnInit {
	results: TestResult[];
	
	constructor(
		private _testResultService: TestResultService,
		private _routeParams: RouteParams) {}

	ngOnInit() {
		let runId = +this._routeParams.get('run_id');
		this._testResultService.getResults().then(results => this.results = results);
	}
}