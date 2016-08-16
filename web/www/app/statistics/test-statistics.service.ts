import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {TestStatistics} from "./test-statistics";


@Injectable()
export class TestStatisticsService {

    constructor(private _http: Http) {}


    getStatistics(testSuiteId: String): Observable<TestStatistics[]> {
        return this._http.get("/api/testsuites/" + testSuiteId + "/statistics")
            .map(TestStatisticsService.extractBody)
            .catch(TestStatisticsService.extractError)
    }

    getStatisticsByTestSuiteAndTestName(testSuiteId: string, testName: string): Observable<TestStatistics> {
        let decodedTestName = encodeURIComponent(testName);
        return this._http.get("/api/testsuites/" + testSuiteId + "/statistics/" + decodedTestName)
            .map(TestStatisticsService.extractBodySingle)
            .catch(TestStatisticsService.extractError)
    }

    private static extractBody(response: Response): TestStatistics[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestStatisticsService.convertJsonToTestStatisticsList(response.json());
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

    private static convertJsonToTestStatisticsList(json: any): TestStatistics[] {
        var testStatisticsList = [];
        for(var testStatisticsJson of json) {
            let testStatistics = TestStatisticsService.convertJsonToTestStatistic(testStatisticsJson)
            testStatisticsList.push(testStatistics);
        }
        return testStatisticsList;
    }

    private static convertJsonToTestStatistic(json: any): TestStatistics {
        var testStatistics = new TestStatistics();
        testStatistics.testSuite = json.testSuite;
        testStatistics.testName = json.testName;
        testStatistics.numPassed = json.numPassed;
        testStatistics.numFailed = json.numFailed;

        return testStatistics;
    }

}