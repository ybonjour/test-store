import {Component, OnInit} from "angular2/core";
import {RouteParams} from "angular2/router";
import {TestResultService} from "../test-result.service";
import {TestWithResults} from "../test-with-results";
import {TestResultsComponent} from "../test-results.component";
import {TestResultsHeaderComponent} from "../header/test-results-header.component";

@Component({
    templateUrl: 'app/test-result/diff/test-result-diff.html',
    styleUrls: ['app/test-result/diff/test-result-diff.css'],
    directives: [TestResultsComponent, TestResultsHeaderComponent]
})
export class TestResultDiffComponent implements OnInit {

    runId:string;
    results:{string:TestWithResults[]} = {};
    numPassedResults:number = 0;
    numRetriedResults:number = 0;
    numFailedResults:number = 0;
    errorMessage:string;


    constructor(private _testResultService:TestResultService,
                private _routeParams:RouteParams) {
    }


    ngOnInit():any {
        this.runId = this._routeParams.get('run_id');
        this._testResultService.getResultsDiff(this.runId).subscribe(
            results => this.extractResults(results),
            error => this.errorMessage = <any>error
        );
    }

    extractResults(results:{string:TestWithResults[]}) {
        this.results = results;
        this.numPassedResults = this.numResultsForCategory(results, "NEW_PASSED")
            + this.numResultsForCategory(results, "FIXED")
            + this.numResultsForCategory(results, "STILL_PASSING");

        this.numRetriedResults = this.numResultsForCategory(results, "NEW_RETRIED")
            + this.numResultsForCategory(results, "RETRIED_AFTER_PASSED")
            + this.numResultsForCategory(results, "RETRIED_AFTER_FAILED")
            + this.numResultsForCategory(results, "STILL_RETRIED");

        this.numFailedResults = this.numResultsForCategory(results, "NEW_FAILED")
            + this.numResultsForCategory(results, "BROKE")
            + this.numResultsForCategory(results, "STILL_FAILING");
    }

    private numResultsForCategory(results:{string:TestWithResults[]}, category:string) {
        if (!(category in results)) return 0;
        return results[category].length;
    }
}