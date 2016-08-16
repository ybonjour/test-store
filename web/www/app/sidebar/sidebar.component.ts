import {Component, OnInit, EventEmitter, Input, Output} from "@angular/core";
import {ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {TestSuite} from "./../test-suite/test-suite";
import {TestSuiteService} from "./../test-suite/test-suite.service";
import {TestSuitesChangedEvent} from "../test-suite/test-suites-changed-event";

@Component({
    selector: 'sidebar',
    templateUrl: 'app/sidebar/sidebar.html',
    styleUrls: ['app/sidebar/sidebar.css'],
    directives: [ROUTER_DIRECTIVES],
})
export class SidebarComponent implements OnInit {
    testSuites: TestSuite[] = [];
    errorMessage: String;

    @Input() sidebarVisible: boolean;
    @Input() currentTestSuiteId: string;
    @Output() hideSidebar: EventEmitter<any> = new EventEmitter<any>();

    constructor(
        private _testSuiteService: TestSuiteService,
        private _testSuitesChangedEvent: TestSuitesChangedEvent){}

    ngOnInit():any {
        this.onTestSuitesChanged();
        //TODO: might cause double invocation
        this._testSuitesChangedEvent.subscribe(this.onTestSuitesChanged, this);
    }

    onHideSidebar(event) {
        this.hideSidebar.emit(event);
    }

    isCurrentTestSuite(testSuite: TestSuite): boolean {
        if(this.currentTestSuiteId == null) return false;
        return this.currentTestSuiteId == testSuite.id;
    }

    onTestSuitesChanged() {
        this._testSuiteService.getTestSuites().subscribe(
            testSuites => this.testSuites = testSuites,
            error => this.errorMessage = <any>error
        )
    }
}