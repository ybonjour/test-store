import {Injectable} from "@angular/core";
import {Http, Response, Headers, RequestOptions} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {TestSuite} from "./test-suite";

@Injectable()
export class TestSuiteService {
    constructor(private _http: Http) {}

    getTestSuites(): Observable<TestSuite[]> {
        return this._http.get("/api/testsuites")
            .map(TestSuiteService.extractTestSuites)
            .catch(TestSuiteService.extractError);
    }

    createTestSuite(name: string): Observable<string> {
        let headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded'})
        let options = new RequestOptions({headers: headers});
        let body = "name=" + name;
        return this._http.post("/api/testsuites", body, options)
            .map(TestSuiteService.extractCreateResponse)
            .catch(TestSuiteService.extractError)
    }

    getTestSuite(id: string): Observable<TestSuite> {
        return this._http.get("/api/testsuites/" + id)
            .map(TestSuiteService.extractTestSuite)
            .catch(TestSuiteService.extractError)
    }

    private static extractCreateResponse(response: Response): string {
        if(response.status != 201) throw new Error("Bad response status: " + response.status);

        return response.json().id
    }

    private static extractTestSuites(response: Response): TestSuite[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestSuiteService.convertToTestSuites(response.json());
    }

    private static extractTestSuite(response: Response): TestSuite {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestSuiteService.convertFromTestSuiteOverview(response.json());
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