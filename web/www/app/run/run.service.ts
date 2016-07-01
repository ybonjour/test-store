import {Injectable} from 'angular2/core'
import {Http, Response} from "angular2/http";
import {Observable} from 'rxjs/Observable';
import {Run} from './run'


@Injectable()
export class RunService {

    constructor(private _http: Http) {}


    getRuns(testSuiteId: String): Observable<Run[]> {
        return this._http.get("/api/testsuites/" + testSuiteId + "/runs/overview")
            .map(RunService.extractBody)
            .catch(RunService.extractError)
    }

    private static extractBody(response: Response): Run[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return RunService.convertJsonToRunList(response.json());
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToRunList(json: any): Run[] {
        var runs = [];
        for(var runJson of json) {
            var run = new Run();
            run.id = runJson.run.id;
            run.testSuite = runJson.run.testSuite;
            run.revision = runJson.run.revision;
            run.time = new Date(runJson.run.time);
            run.runResult = runJson.result;
            run.totalDurationMillis = runJson.totalDurationMillis;
            runs.push(run);
        }

        runs.sort(function(run1, run2) { return run2.time - run1.time });

        return runs;
    }

}