import {Component, OnInit} from "@angular/core";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {HistoryService} from "./history.service";
import {HistoryEntry} from "./history-entry";
import {HistoryTestName} from "./history-test-name";
import {TestResultService} from "../test-result/test-result.service";
import {TestWithResults} from "../test-result/test-with-results";
import {TestResultsComponent} from "../test-result/test-results.component";
import {HistoryFilter} from "./history-filter.pipe";
import {Page} from "../common/page";

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
    testNamesByLongName: {[longName: string]: HistoryTestName} = {};
    nextPage: string = null;
    numEntriesPerPage = 5;
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
        this.nextPage = null;
        this._historyService.getTestnames(this.testSuiteId, this.limit).subscribe(
            testnames => this.handleTestnamesReceived(testnames),
            error => this.errorMessage = <any>error
        );
    }

    private handleTestnamesReceived(testnames: string[]) {
        this.testNames = testnames.map((name) => new HistoryTestName(name));
        for(let testName of this.testNames) {
            this.testNamesByLongName[testName.longName] = testName;
        }
        this.getResults(testnames);
    }

    private getResults(testnames: string[]) {
        this._historyService.getResults(this.testSuiteId, this.nextPage, this.numEntriesPerPage, testnames).subscribe(
            resultPage => this.handleResultsReceived(resultPage, testnames),
            error => this.errorMessage = <any>error
        )
    }

    private handleResultsReceived(resultPage: Page<HistoryEntry>, testnames: string[]) {
        this.historyEntries = this.historyEntries.concat(resultPage.results).slice(0, this.limit);
        this.nextPage = resultPage.nextPage;

        for(let historyEntry of this.historyEntries) {
            for(let testname in historyEntry.results) {
                this.testNamesByLongName[testname].addResult(historyEntry.results[testname]);
            }
        }

        if (this.nextPage && this.historyEntries.length < this.limit) {
            this.getResults(testnames);
        }
    }
}