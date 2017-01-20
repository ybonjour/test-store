import {Component, Input, OnInit} from "@angular/core";
import {RevisionService} from "./revision.service";
import {Revision} from "./revision";

@Component({
    selector: 'revisions',
    templateUrl: 'app/revision/revision-list.html',
    styleUrls: ['app/revision/revision-list.css']
})
export class RevisionListComponent implements OnInit{

    @Input() runId: string;
    expanded: boolean = false;
    revisions: Revision[] = [];
    errorMessage: string;

    constructor(private _revisionService: RevisionService) {}

    ngOnInit():any {
        this._revisionService.getRevisions(this.runId).subscribe(
            revisions => this.revisions = revisions.sort( (r1:Revision, r2:Revision) => r2.time.getTime() - r1.time.getTime()),
            error => this.errorMessage = <any> error
        );
    }
}