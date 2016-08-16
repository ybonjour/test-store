import {Component, OnInit} from "@angular/core";
import {RouteParams} from "@angular/router-deprecated";
import {TestResultService} from "../test-result.service";
import {TestWithResults} from "../test-with-results";
import {TestResultsComponent} from "../test-results.component";
import {TestResultsHeaderComponent} from "../header/test-results-header.component";
import {RevisionComponent} from "../../revision/revision-list.component";

@Component({
	templateUrl: 'app/test-result/list/test-result-list.html',
	directives: [
		TestResultsComponent,
		TestResultsHeaderComponent,
		RevisionComponent]
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

	extractResults(results: {[category: string]: TestWithResults[]}) {
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
}