import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import {TestResult} from "./test-result";
import {TestWithResults} from "./test-with-results";

@Injectable()
export class TestResultService {
	constructor(private _http: Http) {}

	getResultsGrouped(runId: String): Observable<Map<String, TestWithResults[]>> {
		return this._http.get("/api/runs/" + runId + "/results/grouped")
			.map(TestResultService.extractBody)
			.catch(TestResultService.extractError)
	}

	private static extractBody(response: Response) {
		if(response.status != 200) throw new Error("Bad response status: " + response.status);

		return response.json();
	}

	private static extractError (error: any) {
		let errorMessage = error.message || "Server error";
		return Observable.throw(errorMessage);
	}
}