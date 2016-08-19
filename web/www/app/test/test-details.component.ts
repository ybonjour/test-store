import {Component, OnInit} from "@angular/core";
import {RouteParams} from "@angular/router-deprecated";
import {TestResultService} from "../test-result/test-result.service";
import {TestResult} from "../test-result/test-result";
import {TestStatisticsService} from "../statistics/test-statistics.service";
import {TestStatistics} from "../statistics/test-statistics";
import {FORM_DIRECTIVES} from "@angular/forms";
import {CORE_DIRECTIVES, NgClass} from "@angular/common";
import {CHART_DIRECTIVES} from "ng2-charts/ng2-charts";

@Component({
    templateUrl: 'app/test/test-details.html',
    styleUrls: ['app/test/test-details.css'],
    directives: [CHART_DIRECTIVES, NgClass, CORE_DIRECTIVES, FORM_DIRECTIVES]
})
export class TestDetailsComponent implements OnInit{
    testSuiteId: string;
    testName: string;
    results: TestResult[] = [];
    durations: number[];
    labels: string[];
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
        this.durations = [];
        this.labels = [];
        for(let index in results) {
            this.durations.push(results[index].durationMillis);
            this.labels.push(index);
        }
    }
}