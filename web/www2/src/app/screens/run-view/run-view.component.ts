import { Component, OnInit, ViewChild } from '@angular/core';
import { Run } from '../../domain/Run';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { NavigationService } from '../../services/navigation-service/navigation.service';
import { TestService } from '../../services/test-service/test.service';
import { Test } from '../../domain/Test';
import { RunService } from '../../services/run-service/run.service';
import { Testsuite } from '../../domain/Testsuite';
import { TestsuiteService } from '../../services/testsuite-service/testsuite.service';
import { Result } from '../../domain/Result';


const filterFailed = (test: Test, filter: string) => {
  return filter === EMPTY_FILTER_STRING ?
    (test.result === Result.FAILED) :
    (test.result === Result.FAILED && (test.name.trim().toLowerCase().indexOf(filter) !== -1));
};

const filterSuccess = (test: Test, filter: string) => {
  return filter === EMPTY_FILTER_STRING ?
    true :
    test.name.trim().toLowerCase().indexOf(filter) !== -1;
};


const EMPTY_FILTER_STRING = 'e3m1p4t1y5F9i2l6t5e3r5';


@Component({
  selector: 'app-run-view',
  templateUrl: './run-view.component.html',
  styleUrls: ['./run-view.component.scss']
})

export class RunViewComponent implements OnInit {

  private run: Run;
  private testsuite: Testsuite;
  private dataSource: MatTableDataSource<Test>;
  private filterValue: string;
  private filterPredicate;
  private checked: boolean;

  private displayedColumns = ['state', 'name', 'totalDurationMillis'];

  @ViewChild(MatPaginator)
  set dataSourcePaginator(paginator: MatPaginator) {
    if (this.dataSource) {
      this.dataSource.paginator = paginator;
    }
  }

  @ViewChild(MatSort) set sortComponent(sort: MatSort) {
    if (this.dataSource && sort) {
      this.dataSource.sort = sort;
    }
  }

  constructor(private activatedRoute: ActivatedRoute,
              private navigationService: NavigationService,
              private testService: TestService,
              private runService: RunService,
              private testsuiteService: TestsuiteService) {
  }

  ngOnInit() {
    const runId = this.activatedRoute.snapshot.paramMap.get('runId');
    const testsuiteId = this.activatedRoute.snapshot.paramMap.get('testsuiteId');
    this.navigationService.setBackLink('/testsuite/' + testsuiteId);
    this.loadData(runId, testsuiteId);
  }

  private loadData(runId: string, testsuiteId: string): void {
    this.runService.getRun(runId)
      .subscribe(run => {
        this.run = run;
        this.setDefaultFilter();

        this.testService.getResultsOverview(runId)
          .subscribe(response => {
            this.dataSource = new MatTableDataSource<Test>(response);
            this.applyFilter();
          });

      });

    this.testsuiteService.getTestsuite(testsuiteId)
      .subscribe(response => this.testsuite = response);
  }

  private applyFilter() {
    this.dataSource.filterPredicate = this.checked ? filterSuccess : filterFailed;
    this.dataSource.filter = this.filterValue ? this.filterValue.trim().toLowerCase() : EMPTY_FILTER_STRING;
  }

  private clearFilter() {
    this.filterValue = '';
    this.applyFilter();
  }

  private setDefaultFilter() {
    this.checked = this.run.numberOfTestsFailed === 0;
  }
}
