import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource } from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { Testsuite } from '../../domain/Testsuite';
import { TestsuiteService } from '../../services/testsuite-service/testsuite.service';
import { Run } from '../../domain/Run';
import { TranslateService } from '@ngx-translate/core';
import { RunService } from '../../services/run-service/run.service';
import { NavigationService } from '../../services/navigation-service/navigation.service';

@Component({
  selector: 'app-testsuite-view',
  templateUrl: './testsuite-view.component.html',
  styleUrls: ['./testsuite-view.component.scss']
})
export class TestsuiteViewComponent implements OnInit {

  private testsuite: Testsuite;
  private lastRun: Run;
  private dataSource: MatTableDataSource<Run>;

  private displayedColumns = ['state', 'revision', 'device', 'dateTime', 'totalDurationMillis',
    'numberOfTestsTotal', 'numberOfTestsSuccessful', 'numberOfTestsFailed'];


  @ViewChild(MatPaginator) set dataSourcePaginator(paginator: MatPaginator) {
    if (this.dataSource) {
      this.dataSource.paginator = paginator;
    }
  }

  @ViewChild(MatSort) set sortComponent(sort: MatSort) {
    if (this.dataSource && sort) {
      this.dataSource.sort = sort;
    }
  }

  constructor(
    private testsuiteService: TestsuiteService,
    private translateService: TranslateService,
    private runService: RunService,
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private navigationService: NavigationService
  ) {
  }

  ngOnInit() {
    const testsuiteId = this.activatedRoute.snapshot.paramMap.get('testsuiteId');
    this.navigationService.setBackLink('/dashboard');
    this.loadData(testsuiteId);
  }

  private loadData(testsuiteId) {
    this.testsuiteService.getTestsuite(testsuiteId)
      .subscribe(response => this.testsuite = response);

    this.runService.getLastRun(testsuiteId)
      .subscribe(response => this.lastRun = response);

    this.runService.getRunsOverview(testsuiteId)
      .subscribe(runsOverview => {
        this.dataSource = new MatTableDataSource<Run>(runsOverview);
      });
  }

  private navigateToRunView(run: Run) {
    if (!run.isResultUnknown()) {
      this.router.navigate(['/testsuite/' + this.testsuite.id + /runs/ + run.id]);
    }
  }

  private navigateToTestHistory() {
    this.router.navigate((['/testsuite/' + this.testsuite.id + '/history']));
  }
}
