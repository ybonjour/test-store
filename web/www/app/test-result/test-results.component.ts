import {Component, Input, OnInit} from 'angular2/core'
import {TestWithResults} from "./test-with-results";
import {StacktraceComponent} from "./stacktrace/stacktrace.component";

@Component({
    selector: 'test-results',
    templateUrl: 'app/test-result/test-results.html',
    styleUrls: ['app/test-result/test-results.css'],
    directives: [StacktraceComponent]
})
export class TestResultsComponent implements OnInit {
    ngOnInit():any {
        console.log("Results: " + this.results)
        console.log("Type: " + this.type)
    }
    @Input() results: TestWithResults[];
    @Input() type: string;
}