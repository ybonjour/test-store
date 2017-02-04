import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {TestStatistics} from "./test-statistics";
import {Page} from "../common/page";
import {JsonPageExtractor} from "../common/json-page-extractor";


@Injectable()
export class TestStatisticsService {

    constructor(private _http: Http) {}

    getStatisticsPaged(testSuiteId: string, nextPage: string, fetchSize: number): Observable<Page<TestStatistics>> {
        let params: URLSearchParams = new URLSearchParams();
        if(nextPage != null) params.set('page', nextPage);
        if(fetchSize != null) params.set('fetchSize', fetchSize.toString());

        return this._http.get("/api/testsuites/" + testSuiteId + "/statistics-paged", { search: params })
            .map(TestStatisticsService.extractBodyPaged)
            .catch(TestStatisticsService.extractError)
    }

    getStatisticsByTestSuiteAndTestName(testSuiteId: string, testName: string): Observable<TestStatistics> {
        let decodedTestName = encodeURIComponent(testName);
        return this._http.get("/api/testsuites/" + testSuiteId + "/statistics/" + decodedTestName)
            .map(TestStatisticsService.extractBodySingle)
            .catch(TestStatisticsService.extractError)
    }

    private static extractBodyPaged(response: Response): Page<TestStatistics> {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return JsonPageExtractor.extractFromJson(response.json(), TestStatisticsService.convertJsonToTestStatistic)
    }

    private static extractBodySingle(response: Response): TestStatistics {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestStatisticsService.convertJsonToTestStatistic(response.json())
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToTestStatistic(json: any): TestStatistics {
        let testStatistics = new TestStatistics();
        testStatistics.testSuite = json.testSuite;
        testStatistics.testName = json.testName;
        testStatistics.numPassed = json.numPassed;
        testStatistics.numFailed = json.numFailed;
        testStatistics.durationSum = json.durationSum != null ? json.durationSum : 0;
        testStatistics.durationNum = json.durationNum != null ? json.durationNum : 0;

        return testStatistics;
    }
}