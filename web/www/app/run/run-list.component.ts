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
    numEntriesExpected: number = 200;
    numEntriesPerFetch: number = 10;
    errorMessage: string;
    runs: Run[] = [];
    nextPage: string = null;
    testSuiteId: string;
    constructor(
        private _runService: RunService,
        private _routeParams: RouteParams
    ) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.fetchRuns();
    }

    fetchRuns() {
        this._runService.getRuns(this.testSuiteId, this.nextPage, this.numEntriesPerFetch).subscribe(
            runPage => this.extractPage(runPage),
            error => this.errorMessage = <any>error);
    }

    private extractPage(runPage: Page<Run>) {
        this.runs = this.runs.concat(runPage.results);
        this.nextPage = runPage.nextPage;

        if(this.nextPage && this.numEntriesExpected > this.runs.length) {
            this.fetchRuns();
        }
    }

    more() {
        this.numEntriesExpected = 2 * this.numEntriesExpected;
        this.fetchRuns();
    }
}