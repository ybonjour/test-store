import {Component} from 'angular2/core';
import {Injectable} from 'angular2/core';
import {TestResultService} from './test-result.service'

@Component({
  selector: 'test-results',
  template: `
		<h1>Test Results for</h1>
		<ul>
			<li *ngFor="#result of results">
				{{result.testName}}
			</li>
		</ul>
	`
})
export class TestResultsComponent {
	constructor(private _testResultService: TestResultService) {}
	results = _testResultService.getResults();
}