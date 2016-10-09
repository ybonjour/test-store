import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {TestResult} from "./test-result";
import {TestWithResults} from "./test-with-results";
import {Page} from "../common/page";
import {JsonPageExtractor} from "../common/json-page-extractor";

@Injectable()
export class TestResultService {
    constructor(private _http:Http) {
    }

    getResultsByTestSuiteAndTestName(testSuiteId: string, testName: string, nextPage: string): Observable<Page<TestResult>> {
        let params = new URLSearchParams();
        if(nextPage != null) params.set('page', nextPage);

        return this._http.get("/api/testsuites/" + testSuiteId + "/tests/" + encodeURIComponent(testName), {search: params})
            .map(TestResultService.extractUngroupedResults)
            .catch(TestResultService.extractError)
    }

    getResultsDiff(runId:String):Observable<{[id: string]:TestWithResults[]}> {
        return this._http.get("/api/runs/" + runId + "/results/diff")
            .map(TestResultService.extractBody)
            .catch(TestResultService.extractError)
    }

    getResultsGrouped(runId:String):Observable<{[id: string]: TestWithResults[]}> {
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

    private static extractUngroupedResults(response: Response): Page<TestResult> {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return JsonPageExtractor.extractFromJson(response.json(), TestResultService.extractResultFromJson)
    }

    private static extractSingleResult(response: Response): TestWithResults {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestResultService.convertTestWithResultsFromJson(response.json())
    }

    private static extractBody(response:Response): {[category: string]: TestWithResults[]} {
        if (response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestResultService.convertGroupedResults(response.json());
    }

    private static extractError(error:any) {
        let errorMessage = error.message || "Server error";
        return Observable.throw(errorMessage);
    }

    private static convertGroupedResults(json:any): {[category: string]: TestWithResults[]} {
        var result: {[category: string]: TestWithResults[]} = {};
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
        var results = TestResultService.convertResultsFromJson(json.results);

        results.sort(function (result1, result2) {
            return result2.retryNum - result1.retryNum;
        });

        let testWithResults = new TestWithResults();
        testWithResults.testName = json.testName;
        testWithResults.testResult = json.testResult;
        testWithResults.results = results;

        return testWithResults;
    }

    private static extractResultFromJson(json: any): TestResult {
        let result = new TestResult();
        result.run = json.run;
        result.testName = json.testName;
        result.retryNum = json.retryNum;
        result.passed = json.passed;
        result.durationMillis = json.durationMillis;
        result.time = new Date(json.time);
        result.stackTrace = json.stackTrace;
        return result;
    }

    private static convertResultsFromJson(json:any): TestResult[] {
        var results = [];
        for(let resultJson of json) {
            let result = TestResultService.extractResultFromJson(resultJson);
            results.push(result);
        }

        return results;
    }
}