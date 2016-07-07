import {Component, OnInit} from 'angular2/core';
import {RouteParams} from 'angular2/router';

@Component({
    templateUrl: 'app/statistics/test-statistics.html'
})
export class TestStatisticsComponent implements OnInit{
    testSuiteId: string;

    constructor(private _routeParams: RouteParams) { }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id')
    }

}