import {Component, Input} from 'angular2/core'

@Component({
    selector: 'stacktrace',
    templateUrl: 'app/test-result/stacktrace/stacktrace.html',
    styleUrls: ['app/test-result/stacktrace/stacktrace.css']
})
export class StacktraceComponent {
    @Input() stackTrace: String;
    expanded = false;
    showCollapseIcon = false;

    isExpandable() {
        return this.stackTrace != null && this.stackTrace.length > 3500;
    }

    expand() {
        this.expanded = true;
    }

    collapse() {
        this.expanded = false;
        this.showCollapseIcon = false;
    }
}