import {Injectable} from 'angular2/core'
import {Http, Response} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {TestSuite} from "./test-suite";



@Injectable()
export class TestSuiteService {
    constructor(private _http: Http) {}

    getTestSuites(): Observable<TestSuite[]> {
        return this._http.get("/api/testsuites")
            .map(TestSuiteService.extractBody)
            .catch(TestSuiteService.extractError);
    }


    private static extractBody(response: Response): TestSuite[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestSuiteService.mapToTestSuites(response.json());

    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static mapToTestSuites(json: any) {
        var testSuites = [];
        for(var testSuiteJson of json) {
            var testSuite = new TestSuite();
            testSuite.id = testSuiteJson.testSuite.id;
            testSuite.name = testSuiteJson.testSuite.name;
            testSuite.lastRunResult = testSuiteJson.lastRunResult;

            testSuites.push(testSuite);
        }

        return testSuites;
    }

}