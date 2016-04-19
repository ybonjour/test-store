import {Component} from 'angular2/core';
import {Injectable} from 'angular2/core';
import {OnInit} from 'angular2/core';
import { RouteParams } from 'angular2/router';
import {TestResultService} from './test-result.service'
import {TestResult} from './test-result'

@Component({
  selector: 'test-results',
  template: `
		<h1>Test Results for </h1>
		<ul>
			<li *ngFor="#result of results">
				{{result.testName}}
			</li>
		</ul>
	`
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