import {Component} from "angular2/core";
import {HTTP_PROVIDERS} from "angular2/http";
import {RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from "angular2/router";
import {Component} from 'angular2/core';
import {TestResultListComponent} from "./test-result/test-result-list.component.ts";
import {RunListComponent} from "./run/run-list.component.ts";
import {TestResultService} from "./test-result/test-result.service.ts";
import {RunService} from "./run/run.service.ts";
import {TestSuiteService} from "./test-suite/test-suite.service.ts"
import {SidebarComponent} from "./sidebar.component";

@Component({
    selector: 'app',
    templateUrl: 'app/app.html',
    styleUrls: ['app/app.css'],
    directives: [ROUTER_DIRECTIVES, SidebarComponent],
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
export class AppComponent {

    sidebarVisible = true;

    hideSidebar() {
        this.sidebarVisible = false;
    }

    showSidebar() {
        this.sidebarVisible = true;
    }
}