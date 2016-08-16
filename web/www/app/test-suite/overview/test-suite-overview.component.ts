import {Component, OnInit} from "@angular/core";
import {ROUTER_DIRECTIVES, RouteParams} from "@angular/router-deprecated";
import {TestSuite} from "../test-suite";
import {TestSuiteService} from "../test-suite.service";
import {Window} from "../../window";

@Component({
    templateUrl: 'app/test-suite/overview/test-suite-overview.html',
    directives: [ROUTER_DIRECTIVES]
})
export class TestSuiteOverviewComponent implements OnInit{
    testSuite: TestSuite;
    hostname: string;
    errorMessage: string;

    constructor(
        private _testSuiteService: TestSuiteService,
        private _window: Window,
        private _routeParams: RouteParams) {}

    ngOnInit():any {
        this.hostname = this._window.location.hostname;
        let testSuiteId = this._routeParams.get('testsuite_id');
        this._testSuiteService.getTestSuite(testSuiteId).subscribe(
            testSuite => this.testSuite = testSuite,
            error => this.errorMessage = <any>error
        );
    }
}