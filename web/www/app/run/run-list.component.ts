import {Component, OnInit} from "@angular/core";
import {ROUTER_DIRECTIVES, RouteParams} from "@angular/router-deprecated";
import {Run} from "./run";
import {RunService} from "./run.service";
import {Page} from "../common/page";
import {DurationComponent} from "../duration/duration.component";

@Component({
    templateUrl: 'app/run/run-list.html',
    styleUrls: ['app/run/run-list.css'],
    directives: [ROUTER_DIRECTIVES, DurationComponent]
})
export class RunListComponent implements OnInit {
    errorMessage: string;
    runs: Run[] = [];
    nextPage: string;
    testSuiteId: string;
    constructor(
        private _runService: RunService,
        private _routeParams: RouteParams
    ) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.getRuns(this.testSuiteId, null);
    }

    getRuns(testSuiteId: string, nextPage: string) {
        this._runService.getRuns(testSuiteId, nextPage).subscribe(
            runPage => this.extractPage(runPage),
            error => this.errorMessage = <any>error);
    }

    private extractPage(runPage: Page<Run>) {
        this.runs = this.runs.concat(runPage.results);
        this.nextPage = runPage.nextPage;
    }

    more() {
        this.getRuns(this.testSuiteId, this.nextPage);
    }
}