import {Component} from "angular2/core";
import {HTTP_PROVIDERS} from "angular2/http";
import {RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from "angular2/router";
import {Component, OnInit} from 'angular2/core';
import {TestResultListComponent} from "./test-result-list.component";
import {RunListComponent} from "./run-list.component";
import {TestResultService} from "./test-result.service";
import {RunService} from "./run-service";
import {TestSuiteService} from "./test-suite-service"
import {TestSuite} from "./test-suite";

@Component({
    selector: 'app',
    templateUrl: 'app/app.html',
    styleUrls: ['app/app.css'],
    directives: [ROUTER_DIRECTIVES],
    providers: [
        ROUTER_PROVIDERS,
        HTTP_PROVIDERS,
        TestResultService,
        RunService,
        TestSuiteService
    ]
})
@RouteConfig([
    {
        path: '/runs/:run_id/results',
        name: 'Results',
        component: TestResultListComponent
    },
    {
        path: '/testsuites/:testsuite_id/runs',
        name: 'Runs',
        component: RunListComponent
    }
])
export class AppComponent implements OnInit {

    sidebarVisible = true;
    testSuites: TestSuite[] = [];
    errorMessage: String;

    constructor(private _testSuiteService: TestSuiteService) {}
    onToggleSidebar() {
        this.sidebarVisible = !this.sidebarVisible;
    }

    ngOnInit():any {
        this._testSuiteService.getTestSuites().subscribe(
            testSuites => this.testSuites = testSuites,
            error => this.errorMessage = <any>error
        );
    }

}