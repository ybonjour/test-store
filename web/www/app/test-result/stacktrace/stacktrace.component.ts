import {Component, Input} from "@angular/core";

@Component({
    selector: 'stacktrace',
    templateUrl: 'app/test-result/stacktrace/stacktrace.html',
    styleUrls: ['app/test-result/stacktrace/stacktrace.css']
})
export class StacktraceComponent {
    @Input() stackTrace: String;
    @Input() log: String;
    expanded = false;
    showCollapseIcon = false;

    isExpandable(event: Event) {
        return this.stackTrace != null && this.stackTrace.length > 3500;
    }

    expand(event: Event) {
        this.expanded = true;
        event.stopPropagation();
    }

    collapse() {
        this.expanded = false;
        this.showCollapseIcon = false;
    }
}