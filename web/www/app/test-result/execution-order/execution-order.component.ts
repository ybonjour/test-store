import {Component, OnInit} from "@angular/core";
import {TestResultsComponent} from "../test-results.component";
import {TestResultService} from "../test-result.service";
import {RouteParams} from "@angular/router-deprecated";
import {TestWithResults} from "../test-with-results";

@Component({
    templateUrl: 'app/test-result/execution-order/execution-order.html',
    selector: 'execution-order',
    directives: [
        TestResultsComponent
    ]
})
export class ExecutionOrderComponent implements OnInit {
    errorMessage: string;
    runId: string;
    results: TestWithResults[] = [];

    constructor(
        private _testResultService: TestResultService,
        private _routeParams: RouteParams
    ) {

    }

    ngOnInit():void {
        this.runId = this._routeParams.get('run_id');
        this.getResults(this.runId);
    }

    getResults(runId: String) {
        this._testResultService.getResultsGrouped(runId).subscribe(
            results => this.extractResults(results),
            error => this.errorMessage = <any>error);
    }

    extractResults(results: {[category: string]: TestWithResults[]}) {
        this.results = [];
        for(var category in results) {
            this.results = this.results.concat(ExecutionOrderComponent.expandResults(results[category]));
        }

        this.results.sort((r1: TestWithResults, r2: TestWithResults) => { return r1.results[0].time.getTime() - r2.results[0].time.getTime() })
    }

    private static expandResults(testWithResults: TestWithResults[]): TestWithResults[] {
        let expandedResults: TestWithResults[] = [];

        for(let original of testWithResults) {
            for(let result of original.results) {
                let testWithResults = new TestWithResults();
                testWithResults.testName = original.testName;
                testWithResults.testResult = original.testResult;
                testWithResults.results = [result];
                expandedResults.push(testWithResults);
            }
        }

        return expandedResults;
    }

}