import {Component, OnInit, EventEmitter, Input, Output} from 'angular2/core'
import {ROUTER_DIRECTIVES} from "angular2/router";
import {TestSuite} from "./test-suite";
import {TestSuiteService} from "./test-suite-service";

@Component({
    selector: 'sidebar',
    templateUrl: 'app/sidebar.html',
    styleUrls: ['app/sidebar.css'],
    directives: [ROUTER_DIRECTIVES],
})
export class SidebarComponent implements OnInit {
    testSuites: TestSuite[] = [];
    errorMessage: String;

    @Input() sidebarVisible: boolean;
    @Output() hideSidebar: EventEmitter = new EventEmitter();

    constructor(private _testSuiteService: TestSuiteService){}

    ngOnInit():any {
        this._testSuiteService.getTestSuites().subscribe(
            testSuites => this.testSuites = testSuites,
            error => this.errorMessage = <any>error
        )
    }

    onHideSidebar(event) {
        this.hideSidebar.emit(event);
    }
}