import {Injectable} from "@angular/core";
import {Http, Response, URLSearchParams, RequestOptions, Headers} from "@angular/http";
import {HistoryEntry} from "./history-entry";
import {Observable} from "rxjs/Rx";
import {Page} from "../common/page";
import {JsonPageExtractor} from "../common/json-page-extractor";

@Injectable()
export class HistoryService {
    constructor(private _http: Http) {}

    getTestnames(testSuiteId: string, limit: number): Observable<string[]> {
        let params: URLSearchParams = new URLSearchParams();
        params.set('limit', limit.toString());

        return this._http.get("/api/testsuites/" + testSuiteId + "/history/testnames", { search: params })
            .map(HistoryService.extractBodyTestnames)
            .catch(HistoryService.extractError)    }

    getResults(testSuiteId: string, nextPage: string, fetchSize: number, testnames: string[]): Observable<Page<HistoryEntry>> {
        let params: URLSearchParams = new URLSearchParams();
        if (nextPage != null) params.set('page', nextPage);
        if (fetchSize != null) params.set('fetchSize', fetchSize.toString());

        let headers = new Headers({'Content-Type': 'application/json'});
        let options = new RequestOptions({headers: headers, search: params});

        return this._http.post("/api/testsuites/" + testSuiteId + "/history/results", testnames, options)
            .map(HistoryService.extractBodyPaged(testnames))
            .catch(HistoryService.extractError)
    }

    private static extractBodyPaged(testnames: string[]): (Response) => Page<HistoryEntry> {
        return function (response: Response) {
            if(response.status != 200) throw new Error("Bad response status: " + response.status);


            return JsonPageExtractor.extractFromJson(response.json(), HistoryService.convertJsonToHistoryEntriesPaged(testnames));
        }
    }

    private static convertJsonToHistoryEntriesPaged(testnames: string[]): (json: any) => HistoryEntry {
        return function(json: any) {
            let historyEntry = new HistoryEntry();
            historyEntry.revision = json.revision;
            historyEntry.runId = json.runId;
            historyEntry.results = HistoryService.convertJsonToResultMap(json.results2, testnames);
            return historyEntry;
        };
    }

    private static extractBodyTestnames(response: Response): string[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return response.json();
    }

    private static convertJsonToResultMap(json: any, testnames: string[]): {[id: string]: string} {
        let results: {[id: string]: string} = {};

        for(var i in testnames) {
            results[testnames[i]] = json[i];
        }

        return results;
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server errror";
        console.error(errorMessage);
        return Observable.throw(errorMessage)
    }
}
