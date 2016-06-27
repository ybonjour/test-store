import {Component, OnInit} from "angular2/core";
import {HTTP_PROVIDERS} from "angular2/http";
import {Router, RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from "angular2/router";
import {Component} from 'angular2/core';
import {TestResultListComponent} from "./test-result/test-result-list.component.ts";
import {RunListComponent} from "./run/run-list.component.ts";
import {TestResultService} from "./test-result/test-result.service.ts";
import {RunService} from "./run/run.service.ts";
import {TestSuiteService} from "./test-suite/test-suite.service.ts"
import {SidebarComponent} from "./sidebar/sidebar.component.ts";
import {HistoryComponent} from "./history/history.component";
import {AddTestSuiteComponent} from "./test-suite/add-test-suite.component";
import {TestSuitesChangedEvent} from "./test-suite/test-suites-changed-event.ts";

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
        TestSuiteService,
        TestSuitesChangedEvent
    ]
})
@RouteConfig([
    {
        path: '/testsuites/:testsuite_id/runs/:run_id/results',
        name: 'Results',
        component: TestResultListComponent,
    },
    {
        path: '/testsuites/:testsuite_id/runs',
        name: 'Runs',
        component: RunListComponent
    },
    {
        path: '/testsuites/:testsuite_id/history',
        name: 'History',
        component: HistoryComponent
    },
    {
        path: 'testsuites/new',
        name: 'New TestSuite',
        component: AddTestSuiteComponent
    }
])
export class AppComponent implements OnInit {

    constructor(private _router:Router) {

    }
    currentTestSuiteId:string;
    sidebarVisible = true;

    ngOnInit():any {
        this.onNewUrl();
        return this._router.subscribe(
            currentUrl => this.onNewUrl(),
            error => console.error(error)
        );
    }

    private onNewUrl() {
        var instruction = this._router.currentInstruction;
        if(instruction == null) return;
        var component = instruction.component;
        if(component == null) return;
        var params = component.params;
        for(var name in params) {
            if(!params.hasOwnProperty(name)) continue;
            if(name == 'testsuite_id'){
                this.currentTestSuiteId = params[name];
            }
        }
    }

    hideSidebar() {
        this.sidebarVisible = false;
    }

    showSidebar() {
        this.sidebarVisible = true;
    }
}