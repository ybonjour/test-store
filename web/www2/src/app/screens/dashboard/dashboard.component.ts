import { Component, OnInit } from '@angular/core';
import { TestsuiteService } from '../../services/testsuite-service/testsuite.service';
import { Testsuite } from '../../domain/Testsuite';
import { NavigationService } from '../../services/navigation-service/navigation.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  testsuites: Testsuite[];

  constructor(private testsuiteDataService: TestsuiteService,
              private navigationService: NavigationService) {
  }

  ngOnInit() {
    this.navigationService.setBackLink(null);
    this.testsuiteDataService.getTestsuites()
      .subscribe(testsuites => this.testsuites = testsuites);
  }

  testsuitesAvailable(): boolean {
    return this.testsuites != null && this.testsuites.length > 0;
  }
}
