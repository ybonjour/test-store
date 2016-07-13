import {Component, OnInit} from 'angular2/core'
import {RouteParams} from 'angular2/router'
import {TestResultService} from "../test-result/test-result.service";
import {TestResult} from "../test-result/test-result";

@Component({
    templateUrl: 'app/test/test-details.html',
    styleUrls: ['app/test/test-details.css']
})
export class TestDetailsComponent implements OnInit{
    testSuiteId: string;
    testName: string;
    results: TestResult[] = [];
    errorMessage: string;

    constructor(
        private _routeParams: RouteParams,
        private _testResultService: TestResultService) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.testName = this._routeParams.get('test_name');

        this._testResultService.getResultsByTestSuiteAndTestName(this.testSuiteId, this.testName).subscribe(
            results => this.handleResults(results),
            error => this.errorMessage = <any>error
        );
    }

    private handleResults(results:TestResult[]) {
        this.results = results;
    }
}