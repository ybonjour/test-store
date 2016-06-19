import {Component, OnInit} from 'angular2/core';
import {ROUTER_DIRECTIVES} from "angular2/router";
import {Run} from './run';
import {RunService} from './run.service';
import {RouteParams} from 'angular2/router';

@Component({
    templateUrl: 'app/run-list.html',
    styleUrls: ['app/run-list.css'],
    directives: [ROUTER_DIRECTIVES]
})
export class RunListComponent implements OnInit {
    errorMessage: string;
    runs: Run[];
    testSuiteId: string;
    constructor(
        private _runService: RunService,
        private _routeParams: RouteParams
    ) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id')
        this.getRuns(this.testSuiteId)
    }

    getRuns(testSuiteId: string) {
        this._runService.getRuns(testSuiteId).subscribe(
            runs => this.runs = runs,
            error => this.errorMessage = <any>error);
    }
}