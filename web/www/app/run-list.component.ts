import {Component, OnInit} from 'angular2/core';
import {ROUTER_DIRECTIVES} from "angular2/router";
import {Run} from './run';
import {RunService} from './run-service';
import { RouteParams} from 'angular2/router';

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
            json => this.runs = this.convertJson(json),
            error => this.errorMessage = <any>error);
    }

    private convertJson(json: any): Run[] {
        var runs = [];
        for(var runJson of json) {
            var run = new Run();
            run.id = runJson.run.id;
            run.testSuite = runJson.run.testSuite;
            run.revision = runJson.run.revision;
            run.time = new Date(runJson.run.time);
            run.runResult = runJson.result;
            run.totalDurationMillis = runJson.totalDurationMillis;
            runs.push(run);
        }

        runs.sort(function(run1, run2) { return run2.time - run1.time });

        return runs;
    }

}