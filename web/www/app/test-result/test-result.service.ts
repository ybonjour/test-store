import {Injectable} from "angular2/core";
import {Http, Response, URLSearchParams} from "angular2/http";
import {Observable} from "rxjs/Observable";
import {TestResult} from "./test-result";
import {TestWithResults} from "./test-with-results";

@Injectable()
export class TestResultService {
    constructor(private _http:Http) {
    }

    getResultsDiff(runId:String):Observable<{string:TestWithResults[]}> {
        return this._http.get("/api/runs/" + runId + "/results/diff")
            .map(TestResultService.extractBody)
            .catch(TestResultService.extractError)
    }

    getResultsGrouped(runId:String):Observable<Map<String, TestWithResults[]>> {
        return this._http.get("/api/runs/" + runId + "/results/grouped")
            .map(TestResultService.extractBody)
            .catch(TestResultService.extractError)
    }

    getResult(runId: string, testName: string): Observable<TestWithResults> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('testname', encodeURIComponent(testName));

        return this._http.get("/api/runs/" + runId + "/results/filtered", {search: params})
            .map(TestResultService.extractSingleResult)
            .catch(TestResultService.extractError);
    }

    private static extractSingleResult(response: Response): TestWithResults {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestResultService.convertTestWithResultsFromJson(response.json())
    }

    private static extractBody(response:Response) {
        if (response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestResultService.convertGroupedResults(response.json());
    }

    private static extractError(error:any) {
        let errorMessage = error.message || "Server error";
        return Observable.throw(errorMessage);
    }

    private static convertGroupedResults(json:any) {
        var result = {};
        for (var key in json) {
            if (!json.hasOwnProperty(key)) continue;
            result[key] = TestResultService.convertTestsWithResultsFromJson(json[key]);
        }
        return result;
    }

    private static convertTestsWithResultsFromJson(json:any):TestWithResults[] {
        let testsWithResults = [];
        for (var testResultJson of json) {
            let testWithResults = TestResultService.convertTestWithResultsFromJson(testResultJson);
            testsWithResults.push(testWithResults);
        }

        return testsWithResults;
    }

    private static convertTestWithResultsFromJson(json:any):TestWithResults {
        var results = [];
        for (let resultJson of json.results) {
            let result = new TestResult();
            result.run = resultJson.run;
            result.testName = resultJson.testName;
            result.retryNum = resultJson.retryNum;
            result.passed = resultJson.passed;
            result.durationMillis = resultJson.durationMillis;
            result.stackTrace = resultJson.stackTrace;
            results.push(result);
        }

        results.sort(function (result1, result2) {
            return result2.retryNum - result1.retryNum;
        });

        let testWithResults = new TestWithResults();
        testWithResults.testName = json.testName;
        testWithResults.testResult = json.testResult;
        testWithResults.results = results;

        return testWithResults;
    }
}