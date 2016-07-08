import {Injectable} from 'angular2/core'
import {Http, Response} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {TestStatistics} from "./test-statistics";


@Injectable()
export class TestStatisticsService {

    constructor(private _http: Http) {}


    getStatistics(testSuiteId: String): Observable<TestStatistics[]> {
        return this._http.get("/api/testsuites/" + testSuiteId + "/statistics")
            .map(TestStatisticsService.extractBody)
            .catch(TestStatisticsService.extractError)
    }

    private static extractBody(response: Response): TestStatistics[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return TestStatisticsService.convertJsonToTestStatisticsList(response.json());
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToTestStatisticsList(json: any): TestStatistics[] {
        var testStatisticsList = [];
        for(var testStatisticsJson of json) {
            var testStatistics = new TestStatistics();
            testStatistics.testSuite = testStatisticsJson.testSuite;
            testStatistics.testName = testStatisticsJson.testName;
            testStatistics.numPassed = testStatisticsJson.numPassed;
            testStatistics.numFailed = testStatisticsJson.numFailed;
            testStatisticsList.push(testStatistics);
        }
        return testStatisticsList;
    }

}