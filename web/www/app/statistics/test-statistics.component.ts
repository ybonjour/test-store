import {Component, OnInit} from "@angular/core";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {TestStatisticsService} from "./test-statistics.service";
import {TestStatistics} from "./test-statistics";
import {DurationComponent} from "../duration/duration.component";

@Component({
    templateUrl: 'app/statistics/test-statistics.html',
    styleUrls: ['app/statistics/test-statistics.css'],
    directives: [ROUTER_DIRECTIVES, DurationComponent]
})
export class TestStatisticsComponent implements OnInit{
    testSuiteId: string;
    orderBy: string;
    orderDirection: string;
    testStatistics: TestStatistics[];
    errorMessage: string;

    constructor(
        private _testStatisticsService: TestStatisticsService,
        private _routeParams: RouteParams) { }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.orderBy = this._routeParams.get('order_by');
        this.orderDirection = this._routeParams.get('order_direction');
        this._testStatisticsService.getStatistics(this.testSuiteId).subscribe(
            statistics => this.extractStatistics(statistics),
            error => this.errorMessage = <any>error);
    }

    private static getExtractor(orderBy:string): (TestStatistics) => number {
        if(!orderBy) return null;

        if ("duration" == orderBy.toLowerCase()) {
            return s => s.getAverageDuration();
        } else if("passrate" == orderBy.toLowerCase()) {
            return s => s.getPassRate();
        } else {
            return null;
        }
    }

    extractStatistics(testStatistics: TestStatistics[]) {
        this.testStatistics = testStatistics;

        let extractor = TestStatisticsComponent.getExtractor(this.orderBy);
        if(extractor) {
            this.testStatistics.sort(this.comparator(extractor))
        }
    }

    comparator(extractor: (TestStatistics) => number) {
        return (s1: TestStatistics, s2: TestStatistics) => {
            let n1 = extractor(s1);
            let n2 = extractor(s2);
            if(this.orderDirection && this.orderDirection.toLowerCase() == "desc") {
                return n2 - n1;
            } else {
                return n1 - n2;
            }
        }
    }
}