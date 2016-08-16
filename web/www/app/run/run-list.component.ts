import {Component, OnInit} from "@angular/core";
import {ROUTER_DIRECTIVES, RouteParams} from "@angular/router-deprecated";
import {Run} from "./run";
import {RunService} from "./run.service";

@Component({
    templateUrl: 'app/run/run-list.html',
    styleUrls: ['app/run/run-list.css'],
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
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.getRuns(this.testSuiteId);
    }

    getRuns(testSuiteId: string) {
        this._runService.getRuns(testSuiteId).subscribe(
            runs => this.runs = runs,
            error => this.errorMessage = <any>error);
    }
}