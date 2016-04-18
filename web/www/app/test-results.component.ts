import {Component} from 'angular2/core';

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
	results = [{
		"run": "e7add2bc-a2f4-41ab-97ae-a2f210b3a447",
		"testName": "ch.yvu.teststore.common.CassandraRepositoryTest#canSaveAnItem",
		"retryNum": 0,
		"passed": true,
		"durationMillis": 2349
	}];
}