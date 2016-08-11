import {Component, Input, OnInit} from "angular2/core";
import {RevisionService} from "./revision.service";
import {Revision} from "./revision";

@Component({
    selector: 'revisions',
    templateUrl: 'app/revision/revision-list.html',
    styleUrls: ['app/revision/revision-list.css']
})
export class RevisionComponent implements OnInit{

    @Input() runId: string;
    expanded: boolean = true;
    revisions: Revision[] = [];
    errorMessage: string;

    constructor(private _revisionService: RevisionService) {}

    ngOnInit():any {
        this._revisionService.getRevisions(this.runId).subscribe(
            revisions => this.revisions = revisions,
            error => this.errorMessage = <any> error
        );
    }
}