import {NgModule} from "@angular/core";
import {BrowserModule} from "@angular/platform-browser";
import {AppComponent} from "./app.component";
import {HistoryComponent} from "./history/history.component";
import {RevisionComponent} from "./revision/revision-list.component";
import {RunListComponent} from "./run/run-list.component";
import {SidebarComponent} from "./sidebar/sidebar.component";
import {TestStatisticsComponent} from "./statistics/test-statistics.component";
import {TestDetailsComponent} from "./test/test-details.component";
import {TestResultDiffComponent} from "./test-result/diff/test-result-diff.component";
import {TestResultsHeaderComponent} from "./test-result/header/test-results-header.component";
import {TestResultListComponent} from "./test-result/list/test-result-list.component";
import {StacktraceComponent} from "./test-result/stacktrace/stacktrace.component";
import {TestResultsComponent} from "./test-result/test-results.component";
import {AddTestSuiteComponent} from "./test-suite/add/add-test-suite.component";
import {TestSuiteOverviewComponent} from "./test-suite/overview/test-suite-overview.component";

@NgModule({
    imports:      [ BrowserModule ],
    declarations: [
        AppComponent,
        HistoryComponent,
        RevisionComponent,
        RunListComponent,
        SidebarComponent,
        TestStatisticsComponent,
        TestDetailsComponent,
        TestResultDiffComponent,
        TestResultsHeaderComponent,
        TestResultListComponent,
        StacktraceComponent,
        TestResultsComponent,
        AddTestSuiteComponent,
        TestSuiteOverviewComponent
    ],
    bootstrap:    [ AppComponent ]
})
export class AppModule { }