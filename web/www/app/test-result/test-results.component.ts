import {Component, Input, OnInit} from "@angular/core";
import {TestWithResults} from "./test-with-results";
import {StacktraceComponent} from "./stacktrace/stacktrace.component";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {TestResultService} from "./test-result.service";
import {TestResult} from "./test-result";
import {FailureReason} from "./failure-reason";

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

    constructor(
        private _testResultService: TestResultService,
        private _routeParams: RouteParams) { }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
    }

    updateFailureReason(result: TestResult, failureReason: string) {
        result.failureReason = failureReason;
        this._testResultService.updateFailureReason(result);
    }

    failureReasons(): FailureReason[] {
        return FailureReason.all()
    }
}