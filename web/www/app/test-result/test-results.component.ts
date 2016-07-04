import {Component, Input} from 'angular2/core'
import {TestWithResults} from "./test-with-results";
import {StacktraceComponent} from "./stacktrace/stacktrace.component";

@Component({
    selector: 'test-results',
    templateUrl: 'app/test-result/test-results.html',
    styleUrls: ['app/test-result/test-results.css'],
    directives: [StacktraceComponent]
})
export class TestResultsComponent {
    @Input() results: TestWithResults[];
    @Input() type: string;
}