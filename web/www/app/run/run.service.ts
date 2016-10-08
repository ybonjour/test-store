import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Run} from "./run";
import {RunPage} from "./run-page";
import {JsonPageExtractor} from "../common/json-page-extractor";
import {Page} from "../common/page";

@Injectable()
export class RunService {

    constructor(private _http: Http) {}

    getRuns(testSuiteId: String, nextPage: string): Observable<Page<Run>> {

        let params: URLSearchParams = new URLSearchParams();
        if(nextPage != null) params.set('page', nextPage);


        return this._http.get("/api/testsuites/" + testSuiteId + "/runs/overview", {
            search: params
        })
            .map(RunService.extractBodyPaged)
            .catch(RunService.extractError)
    }

    private static extractBodyPaged(response: Response): Page<Run> {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return JsonPageExtractor.extractFromJson(response.json(), RunService.convertJsonToRun);
        // return RunService.convertJsonToRunPage(response.json());
    }

    private static convertJsonToRunPage(json: any): RunPage {
        let runPage = new RunPage();
        runPage.runs = RunService.convertJsonToRunList(json.results);
        runPage.nextPage = json.nextPage;
        return runPage;
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToRunList(json: any): Run[] {
        var runs = [];
        for(var runJson of json) {
            let run = RunService.convertJsonToRun(runJson);
            runs.push(run);
        }

        runs.sort(function(run1, run2) { return run2.time - run1.time });

        return runs;
    }

    private static convertJsonToRun(runJson: any): Run {
        let run = new Run();
        run.id = runJson.run.id;
        run.testSuite = runJson.run.testSuite;
        run.revision = runJson.run.revision;
        run.time = new Date(runJson.run.time);
        run.runResult = runJson.result;
        run.totalDurationMillis = runJson.totalDurationMillis;

        return run;
    }

}