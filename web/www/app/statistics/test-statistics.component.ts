import {Component, OnInit} from 'angular2/core';
import {RouteParams} from 'angular2/router';
import {TestStatisticsService} from "./test-statistics.service";
import {TestStatistics} from "./test-statistics";

@Component({
    templateUrl: 'app/statistics/test-statistics.html'
})
export class TestStatisticsComponent implements OnInit{
    testSuiteId: string;
    testStatistics: TestStatistics[];
    errorMessage: string;

    constructor(
        private _testStatisticsService: TestStatisticsService,
        private _routeParams: RouteParams) { }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id')
        this._testStatisticsService.getStatistics(this.testSuiteId).subscribe(
            statistics => this.testStatistics = statistics,
            error => this.errorMessage = <any>error);
    }

}