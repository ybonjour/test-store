import {Component, OnInit} from "@angular/core";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {HistoryService} from "./history.service";
import {HistoryEntry} from "./history-entry";
import {HistoryTestName} from "./history-test-name";
import {TestResultService} from "../test-result/test-result.service";
import {TestWithResults} from "../test-result/test-with-results";
import {TestResultsComponent} from "../test-result/test-results.component";
import {HistoryFilter} from "./history-filter.pipe";

@Component({
    templateUrl: 'app/history/history.html',
    styleUrls: ['app/history/history.css'],
    directives: [ROUTER_DIRECTIVES, TestResultsComponent],
    pipes: [HistoryFilter]
})
export class HistoryComponent implements OnInit {
    testSuiteId: string;
    limit: number;
    historyEntries: HistoryEntry[] = [];
    testNames: HistoryTestName[] = [];
    errorMessage: string;
    hidePassed = false;
    
    currentResult: TestWithResults = null;

    constructor(
        private _routeParams: RouteParams,
        private _historyService: HistoryService,
        private _testResultService: TestResultService) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.limit = +this._routeParams.get('limit');
        if(this.limit == 0){
            this.limit = 25;
        }
        this.getHistory();
    }
    
    setLimit(limit: number) {
        this.limit = limit;
        this.getHistory()
    }

    selectResult(runId: string, testName: string){
        this._testResultService.getResult(runId, testName).subscribe(
            result => this.currentResult = result
        )
    }

    unselectResult(event: Event) {
        this.currentResult = null;
        event.stopPropagation();
    }

    isCurrentResult(runId: string, testName: string): boolean{
        if(this.currentResult == null) return false;

        return this.currentResult.getRunId() == runId &&
            this.currentResult.testName == testName
    }

    togglePassed() {
        this.hidePassed = !this.hidePassed;
    }

    encode(input:string) {
        return encodeURIComponent(input)
    }

    private getHistory() {
        this._historyService.getHistory(this.testSuiteId, this.limit).subscribe(
            historyEntries => this.handleNewHistory(historyEntries),
            error => this.errorMessage = <any>error
        )
    }

    private handleNewHistory(historyEntries: HistoryEntry[]) {
        this.loadShortTestNames(historyEntries);
        this.historyEntries = historyEntries;
    }

    private loadShortTestNames(historyEntries: HistoryEntry[]) {

        let tests = {};
        for(let historyEntry of historyEntries) {
            for(let test in historyEntry.results){
                if(!(test in tests)){
                    tests[test] = new HistoryTestName(test);
                }
                tests[test].addResult(historyEntry.results[test])
                this.testNames.push(new HistoryTestName(test));
            }
        }

        this.testNames = [];
        for(let test in tests){
            this.testNames.push(tests[test]);
        }
    }
}