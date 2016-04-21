import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';
import {Observable} from 'rxjs/Observable';
import {TestResult} from './test-results.ts'

@Injectable()
export class TestResultService {
	constructor(private _http: Http) {}

	getResults(runId: String): Observable<TestResult[]> {
		return this._http.get("/api/results")
						.map(this.extractBody)
						.catch(this.extractError);
	}

	private extractBody(response: Response) {
		if(response.status != 200) throw new Error("Bad response status: " + response.status);

		let body = response.json();
		return body.data || {};
	}

	private extractError (error: any) {
		let errorMessage = error.message || "Server error";
		return Observable.throw(errorMessage);
	}
}