import { Component, OnInit, ViewChild } from '@angular/core';
import { Testsuite } from '../../domain/Testsuite';
import { ActivatedRoute } from '@angular/router';
import { NavigationService } from '../../services/navigation-service/navigation.service';
import { TestsuiteService } from '../../services/testsuite-service/testsuite.service';
import { HistoryService } from '../../services/history-service/history.service';
import { TestHistory } from '../../domain/TestHistory';
import { RevisionResult } from '../../domain/RevisionResult';
import { MatPaginator, MatTableDataSource } from '@angular/material';


const filterSuccess = (testHistory: TestHistory, filter: string) => true;

const filterFailed = (testHistory: TestHistory, filter: string) => {
  let valid = false;
  testHistory.revisionResults.forEach(revisionResult => {
    if (revisionResult.isResultFailed() || revisionResult.isResultPartiallyFailed()) {
      valid = true;
      return;
    }
  });
  return valid;
};


@Component({
  selector: 'app-history-grid',
  templateUrl: './history-grid.component.html',
  styleUrls: ['./history-grid.component.scss']
})
export class HistoryGridComponent implements OnInit {

  private testsuite: Testsuite;
  private revisions: string[];
  private dataSource: MatTableDataSource<TestHistory>;
  private columns: string[];
  private checked: boolean;

  @ViewChild(MatPaginator) set dataSourcePaginator(paginator: MatPaginator) {
    if (this.dataSource) {
      this.dataSource.paginator = paginator;
    }
  }

  constructor(private activatedRoute: ActivatedRoute,
              private navigationService: NavigationService,
              private testsuiteService: TestsuiteService,
              private historyService: HistoryService) {
  }

  ngOnInit() {
    const testsuiteId = this.activatedRoute.snapshot.paramMap.get('testsuiteId');
    this.navigationService.setBackLink('/testsuite/' + testsuiteId);
    this.checked = false;
    this.loadData(testsuiteId);
  }

  private loadData(testsuiteId: string) {
    this.testsuiteService.getTestsuite(testsuiteId)
      .subscribe(response => this.testsuite = response);

    this.historyService.getTestNames(testsuiteId)
      .subscribe(testNames => {

        this.historyService.getHistoryOfRuns(testsuiteId, testNames)
          .subscribe(testHistories => {
            this.revisions = testHistories.revisions;
            this.dataSource = new MatTableDataSource<TestHistory>(testHistories.testHistories);
            this.setColumns();
            this.applyFilter();
          });
      });
  }

  private getRevisionResultFor(test: TestHistory, revision: string): RevisionResult {
    return test.revisionResults
      .find(revisionResult => revisionResult.revisionId === revision);
  }

  private setColumns() {
    this.columns = [...this.revisions];
    this.columns.unshift('name');
  }

  private applyFilter() {
    this.dataSource.filterPredicate = this.checked ? filterSuccess : filterFailed;
    this.dataSource.filter = 'FILTER';
  }
}
