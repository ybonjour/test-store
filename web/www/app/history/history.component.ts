import {Component, OnInit} from 'angular2/core'
import {RouteParams, ROUTER_DIRECTIVES} from 'angular2/router';
import {HistoryService} from "./history.service";
import {HistoryEntry} from "./history-entry";
import {HistoryTestName} from "./history-test-name";

@Component({
    templateUrl: 'app/history/history.html',
    styleUrls: ['app/history/history.css'],
    directives: [ROUTER_DIRECTIVES]
})
export class HistoryComponent implements OnInit {
    testSuiteId: string;
    limit: number;
    historyEntries: HistoryEntry[] = [];
    testNames: HistoryTestName[] = [];
    errorMessage: string;
    constructor(
        private _routeParams: RouteParams,
        private _historyService: HistoryService) {}

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
        this.testNames = [];
        let seenTests = {};
        for(let historyEntry of historyEntries) {
            for(let test in historyEntry.results){
                if(test in seenTests) continue;
                seenTests[test] = true;
                this.testNames.push(new HistoryTestName(test));
            }
        }
    }
}