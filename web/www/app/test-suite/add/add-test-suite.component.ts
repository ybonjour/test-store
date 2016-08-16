import {Component} from "@angular/core";
import {TestSuite} from "./../test-suite";
import {TestSuiteService} from "./../test-suite.service";
import {Router} from "@angular/router";
import {TestSuitesChangedEvent} from "./../test-suites-changed-event";

@Component({
    templateUrl: "app/test-suite/add/add-test-suite.html"
})
export class AddTestSuiteComponent{
    testSuite: TestSuite = new TestSuite();
    errorMessage: string;

    constructor(
        private _testSuiteService:TestSuiteService,
        private _router: Router,
        private _testSuitesChangedEvent: TestSuitesChangedEvent) {}

    onAdd() {
        if(this.testSuite.name == null || this.testSuite.name == ""){
            this.errorMessage = "Test suite name can not be empty.";
            return;
        }

        this._testSuiteService.createTestSuite(this.testSuite.name).subscribe(
            testSuiteId => this.onTestSuiteCreated(testSuiteId),
            error => this.errorMessage = <any>error
        );

    }

    private onTestSuiteCreated(testSuiteId: string) {
        this._testSuitesChangedEvent.notify();
        this._router.navigate(['Runs', {testsuite_id: testSuiteId}]);
    }
}