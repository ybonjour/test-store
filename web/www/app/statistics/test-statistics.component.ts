import {Component, OnInit} from "@angular/core";
import {RouteParams, ROUTER_DIRECTIVES} from "@angular/router-deprecated";
import {TestStatisticsService} from "./test-statistics.service";
import {TestStatistics} from "./test-statistics";
import {DurationComponent} from "../duration/duration.component";
import {Page} from "../common/page";

@Component({
    templateUrl: 'app/statistics/test-statistics.html',
    styleUrls: ['app/statistics/test-statistics.css'],
    directives: [ROUTER_DIRECTIVES, DurationComponent]
})
export class TestStatisticsComponent implements OnInit{
    testSuiteId: string;
    orderBy: string;
    orderDirection: string;
    nextPage: string = null;
    fetchSize: number = 10;
    testStatistics: TestStatistics[] = [];
    errorMessage: string;

    constructor(
        private _testStatisticsService: TestStatisticsService,
        private _routeParams: RouteParams) { }

    ngOnInit():any {
        this.testSuiteId = this._routeParams.get('testsuite_id');
        this.orderBy = this._routeParams.get('order_by');
        this.orderDirection = this._routeParams.get('order_direction');
        this.getStatistics(this.testSuiteId);
    }

    getStatistics(testSuiteId: string) {
        this._testStatisticsService.getStatisticsPaged(this.testSuiteId, this.nextPage, this.fetchSize).subscribe(
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

    extractStatistics(testStatisticsPage: Page<TestStatistics>) {
        this.testStatistics = this.testStatistics.concat(testStatisticsPage.results);
        this.nextPage = testStatisticsPage.nextPage;
        let extractor = TestStatisticsComponent.getExtractor(this.orderBy);
        if(extractor) {
            this.testStatistics.sort(this.comparator(extractor))
        }

        if(this.nextPage != null) {
            this.getStatistics(this.testSuiteId);
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

    encode(input:string) {
        return encodeURIComponent(input)
    }
}