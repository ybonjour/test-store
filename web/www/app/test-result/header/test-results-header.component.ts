import {Component, Input, OnInit} from "@angular/core";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";

@Component({
    selector: 'test-results-header',
    templateUrl: 'app/test-result/header/test-results-header.html',
    styleUrls: ['app/test-result/header/test-results-header.css'],
    directives: [ROUTER_DIRECTIVES]
})
export class TestResultsHeaderComponent implements OnInit {
    @Input() numPassedResults:number;
    @Input() numRetriedResults: number;
    @Input() numFailedResults: number;

    testSuiteId: string;
    runId: string;

    constructor(
        private _routeParams: RouteParams) {}

    ngOnInit():void {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.runId = this._routeParams.get('run_id')
    }
}