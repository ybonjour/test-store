import {Component, OnInit} from 'angular2/core'
import {RouteParams} from 'angular2/router'

@Component({
    templateUrl: 'app/test/test-details.html',
    styleUrls: ['app/test/test-details.css']
})
export class TestDetailsComponent implements OnInit{
    testSuiteId: string;
    testName: string;

    constructor(private _routeParams: RouteParams) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.testName = this._routeParams.get('test_name');
        return undefined;
    }
}