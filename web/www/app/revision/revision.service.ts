import {Injectable} from "@angular/core";
import {Http, Response} from "@angular/http";
import {Observable} from "rxjs/Observable";
import {Revision} from "./revision";

@Injectable()
export class RevisionService {

    constructor(private _http: Http) {}

    getRevisions(runId: String): Observable<Revision[]> {
        return this._http.get("/api/runs/" + runId + "/revisions")
            .map(RevisionService.extractBody)
            .catch(RevisionService.extractError)
    }

    private static extractBody(response:Response): Revision[] {
        if(response.status != 200) throw new Error("Bad response status: " + response.status);


        return RevisionService.convertJsonToRevisionList(response.json());
    }

    private static extractError(error: any) {
        let errorMessage = error.message || "Server error";
        console.error(errorMessage);
        return Observable.throw(errorMessage);
    }

    private static convertJsonToRevisionList(json: any): Revision[] {
        var revisions = [];
        for(var revisionJson of json) {
            var revision = new Revision();
            revision.run = revisionJson.run;
            revision.revision = revisionJson.revision;
            revision.time = revisionJson.time;
            revision.author = revisionJson.author;
            revision.comment = revisionJson.comment;
            revision.url = revisionJson.url;

            revisions.push(revision);
        }

        return revisions;
    }

}