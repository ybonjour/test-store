import {Component, Input, OnInit} from "angular2/core";
import {TestWithResults} from "./test-with-results";
import {StacktraceComponent} from "./stacktrace/stacktrace.component";
import {RouteParams, ROUTER_DIRECTIVES} from "angular2/router";

@Component({
    selector: 'test-results',
    templateUrl: 'app/test-result/test-results.html',
    styleUrls: ['app/test-result/test-results.css'],
    directives: [StacktraceComponent, ROUTER_DIRECTIVES]
})
export class TestResultsComponent implements OnInit {
    @Input() results: TestWithResults[];
    @Input() type: string;
    testSuiteId: string;

    constructor(private _routeParams: RouteParams) {}

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
    }
}