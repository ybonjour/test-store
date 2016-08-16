import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {HistoryEntry} from "./history-entry";
import {Observable} from "rxjs/Rx";

@Injectable()
export class HistoryService {
    constructor(private _http: Http) {}

    getHistory(testsuite_id: string, limit: number): Observable<HistoryEntry[]> {
        return this._http.get("/api/testsuites/" + testsuite_id + "/history?limit=" + limit)
            .map(HistoryService.extractBody)
            .catch(HistoryService.extractError)
    }


    private static extractBody(response: Response): HistoryEntry[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);

        return HistoryService.convertJsonToHistoryEntries(response.json());
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server errror";
        console.error(errorMessage);
        return Observable.throw(errorMessage)
    }

    private static convertJsonToHistoryEntries(json: any): HistoryEntry[] {
        var history = [];
        for(var historyEntryJson of json) {
            let historyEntry = new HistoryEntry();
            historyEntry.revision = historyEntryJson.revision;
            historyEntry.runId = historyEntryJson.runId;
            historyEntry.results = HistoryService.convertJsonToResultMap(historyEntryJson.results);
            history.push(historyEntry);
        }

        return history;
    }

    private static convertJsonToResultMap(json: any): { [id: string]: string} {
        let results: {[id: string]: string} = {};

        for(var testName in json){
            if(!json.hasOwnProperty(testName)) continue;

            results[testName] = json[testName];
        }

        return results;
    }


}
