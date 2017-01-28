import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {HistoryEntry} from "./history-entry";
import {Observable} from "rxjs/Rx";

@Injectable()
export class HistoryService {
    constructor(private _http: Http) {}

    getHistory(testsuite_id: string, limit: number): Observable<HistoryEntry[]> {
        return this._http.get("/api/testsuites/" + testsuite_id + "/history-new?limit=" + limit)
            .map(HistoryService.extractBody)
            .catch(HistoryService.extractError)
    }

    private static extractBody(response: Response): HistoryEntry[]{
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return HistoryService.convertJsonToHistoryEntries(response.json());
    }

    private static convertJsonToHistoryEntries(json: any): HistoryEntry[] {
        let history = [];
        let testnames = json.testnames;
        for(var historyEntryJson of json.runHistory) {
            let historyEntry = new HistoryEntry();
            historyEntry.revision = historyEntryJson.revision;
            historyEntry.runId = historyEntryJson.runId;
            historyEntry.results = HistoryService.convertJsonToResultMap(historyEntryJson.results2, testnames);

            history.push(historyEntry);
        }

        return history;
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
