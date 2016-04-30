import {Component} from 'angular2/core';
import {ROUTER_DIRECTIVES} from "angular2/router";

@Component({
    templateUrl: 'app/run-list.html',
    directives: [ROUTER_DIRECTIVES]
})
export class RunListComponent {
    runs = [{"id": "dfkjdkfd"}]
}