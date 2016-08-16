import {Component, OnInit} from "@angular/core";
import {RouteParams} from "@angular/router-deprecated";
import {TestResultService} from "../test-result/test-result.service";
import {TestResult} from "../test-result/test-result";
import {TestStatisticsService} from "../statistics/test-statistics.service";
import {TestStatistics} from "../statistics/test-statistics";

@Component({
    templateUrl: 'app/test/test-details.html',
    styleUrls: ['app/test/test-details.css']
})
export class TestDetailsComponent implements OnInit{
    testSuiteId: string;
    testName: string;
    results: TestResult[] = [];
    statistics: TestStatistics;
    errorMessage: string;

    constructor(
        private _routeParams: RouteParams,
        private _testResultService: TestResultService,
        private _testStatisticsService: TestStatisticsService) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.testName = this._routeParams.get('test_name');

        this._testResultService.getResultsByTestSuiteAndTestName(this.testSuiteId, this.testName).subscribe(
            results => this.handleResults(results),
            error => this.errorMessage = <any>error
        );

        this._testStatisticsService.getStatisticsByTestSuiteAndTestName(this.testSuiteId, this.testName).subscribe(
            statistics => this.statistics = statistics,
            error => this.errorMessage = <any>error
        )
    }

    private handleResults(results:TestResult[]) {
        this.results = results;
    }
}