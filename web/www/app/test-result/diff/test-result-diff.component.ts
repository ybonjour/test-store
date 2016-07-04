import {Component, OnInit} from "angular2/core";
import {RouteParams} from "angular2/router";
import {TestResultService} from "../test-result.service";
import {TestWithResults} from "../test-with-results";
import {TestResultsComponent} from "../test-results.component";

@Component({
    templateUrl: 'app/test-result/diff/test-result-diff.html',
    directives: [TestResultsComponent]
})
export class TestResultDiffComponent implements OnInit {

    runId:string;
    results:{string:TestWithResults[]} = {};
    errorMessage:string;


    constructor(private _testResultService:TestResultService,
                private _routeParams:RouteParams) {
    }


    ngOnInit():any {
        this.runId = this._routeParams.get('run_id');
        this._testResultService.getResultsDiff(this.runId).subscribe(
            results => this.results = results,
            error => this.errorMessage = <any>error
        );
    }
}