import {Component, Input} from "@angular/core";

@Component({
    selector: 'test-results-header',
    templateUrl: 'app/test-result/header/test-results-header.html',
    styleUrls: ['app/test-result/header/test-results-header.css']
})
export class TestResultsHeaderComponent {
    @Input() numPassedResults:number;
    @Input() numRetriedResults: number;
    @Input() numFailedResults: number;
}