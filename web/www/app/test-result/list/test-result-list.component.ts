import {Component, OnInit} from "@angular/core";
import {RouteParams} from "@angular/router-deprecated";
import {TestResultService} from "../test-result.service";
import {TestWithResults} from "../test-with-results";
import {TestResultsComponent} from "../test-results.component";
import {TestResultsHeaderComponent} from "../header/test-results-header.component";
import {RevisionListComponent} from "../../revision/revision-list.component";

@Component({
	templateUrl: 'app/test-result/list/test-result-list.html',
	directives: [
		TestResultsComponent,
		TestResultsHeaderComponent,
		RevisionListComponent]
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

	getResults(runId: string) {
		this._testResultService.getResults(runId, "FAILED")
					.subscribe(
						results => this.failedResults = results,
						error => this.errorMessage = <any>error);

		this._testResultService.getResults(runId, "RETRIED")
			.subscribe(
				results => this.retriedResults = results,
				error => this.errorMessage = <any>error);

		this._testResultService.getResults(runId, "PASSED")
			.subscribe(
				results => this.passedResults = results,
				error => this.errorMessage = <any>error);
	}
}