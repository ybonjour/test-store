import {Injectable} from 'angular2/core'
import {Http, Response, Headers, RequestOptions} from "angular2/http";
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

    createTestSuite(name: String): Observable<String> {
        let headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'})
        let options = new RequestOptions({headers: headers});
        let body = "name=" + name;
        return this._http.post("/api/testsuites", body, options)
            .map(TestSuiteService.extractCreateResponse)
            .catch(TestSuiteService.extractError)
    }

    private static extractCreateResponse(response: Response): String {
        if(response.status != 201) throw new Error("Bad response status: " + response.status);

        return response.json().id
    }

    private static extractBody(response: Response): TestSuite[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestSuiteService.convertToTestSuites(response.json());

    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertToTestSuites(json: any) {
        var testSuites = [];
        for(var testSuiteJson of json) {
            var testSuite = TestSuiteService.convertFromTestSuiteOverview(testSuiteJson);
            testSuites.push(testSuite);
        }

        return testSuites;
    }

    private static convertFromTestSuiteOverview(json: any): TestSuite {
        var testSuite = new TestSuite();
        testSuite.id = json.testSuite.id;
        testSuite.name = json.testSuite.name;
        testSuite.lastRunResult = json.lastRunResult;
        return testSuite;
    }

}