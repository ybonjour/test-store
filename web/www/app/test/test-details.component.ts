import {Component, OnInit} from "@angular/core";
import {RouteParams} from "@angular/router-deprecated";
import {TestResultService} from "../test-result/test-result.service";
import {TestResult} from "../test-result/test-result";
import {TestStatisticsService} from "../statistics/test-statistics.service";
import {TestStatistics} from "../statistics/test-statistics";
import {FORM_DIRECTIVES} from "@angular/forms";
import {CORE_DIRECTIVES, NgClass} from "@angular/common";
import {Page} from "../common/page";
import {TestResultsComponent} from "../test-result/test-results.component";
import {TestWithResults} from "../test-result/test-with-results";

@Component({
    templateUrl: 'app/test/test-details.html',
    styleUrls: ['app/test/test-details.css'],
    directives: [NgClass, CORE_DIRECTIVES, FORM_DIRECTIVES, TestResultsComponent],
})
export class TestDetailsComponent implements OnInit {
    testSuiteId:string;
    testName:string;
    results:TestResult[] = [];
    durations:number[];
    durationsSuggestedMax:number;
    labels:string[];
    pointBackgroundColors:string[];
    statistics:TestStatistics;
    errorMessage:string;

    currentResult:TestResult = null;

    constructor(private _routeParams:RouteParams,
                private _testResultService:TestResultService,
                private _testStatisticsService:TestStatisticsService) {
    }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.testName = decodeURIComponent(this._routeParams.get('test_name'));


        this._testResultService.getResultsByTestSuiteAndTestName(this.testSuiteId, this.testName, null).subscribe(
            results => this.handleResults(results),
            error => this.errorMessage = <any>error
        );

        this._testStatisticsService.getStatisticsByTestSuiteAndTestName(this.testSuiteId, this.testName).subscribe(
            statistics => this.statistics = statistics,
            error => this.errorMessage = <any>error
        )
    }

    selectResult(testResult:TestResult) {
        this.currentResult = testResult;
    }

    unselectResult(event:Event) {
        this.currentResult = null;
        event.stopPropagation();
    }

    stopEventPropagation(event:Event) {
        event.stopPropagation();
    }

    isCurrentResult(result:TestResult):boolean {
        return result == this.currentResult
    }

    private singleResult(testResult:TestResult):TestWithResults {
        let result = new TestWithResults();
        result.testName = testResult.testName;
        result.testResult = testResult.passed ? "PASSED" : "FAILED";
        result.results = [testResult];

        return result;
    }

    private handleResults(page:Page<TestResult>) {
        this.results = page.results;
        this.durations = page.results.map((r) => {
            return r.durationMillis / 1000
        });
        this.labels = page.results.map((r) => {
            return r.time.toLocaleDateString() + " " + r.time.toLocaleTimeString();
        });

        this.durationsSuggestedMax = Math.max.apply(null, this.durations) * 1.2;
        this.pointBackgroundColors = [];

        for (let result of page.results) {
            if (result.passed) {
                this.pointBackgroundColors.push("#00AA00");
            } else {
                this.pointBackgroundColors.push("#AA0000");
            }
        }
    }
}