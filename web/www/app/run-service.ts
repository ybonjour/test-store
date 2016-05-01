import {Injectable} from 'angular2/core'
import {Http, Response} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {Run} from './run'


@Injectable()
export class RunService {

    constructor(private _http: Http) {}

    getRuns(testSuiteId: String): Observable<Run[]> {
        return this._http.get("/api/testsuites/" + testSuiteId + "/runs")
            .map(RunService.extractBody)
            .catch(RunService.extractError)
    }

    private static extractBody(response: Response) {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return response.json();
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

}