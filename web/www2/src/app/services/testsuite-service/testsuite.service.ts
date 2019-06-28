import { Injectable } from '@angular/core';
import { Testsuite } from '../../domain/Testsuite';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { TestsuiteData } from './interfaces/TestsuiteData';
import { map } from 'rxjs/operators';
import { Result } from '../../domain/Result';
import { ConfigurationService } from '../configuration-service/configuration.service';


@Injectable({
  providedIn: 'root'
})

export class TestsuiteService {

  readonly baseUrl = this.configurationService.getBaseUrl();

  static mapResponseToTestsuite(response: TestsuiteData): Testsuite {
    const testsuite = new Testsuite();
    testsuite.id = response.testSuite.id;
    testsuite.name = response.testSuite.name;
    switch (response.lastRunResult) {
      case 'PASSED':
        testsuite.lastRunResult = Result.SUCCESSFUL;
        break;
      case 'UNKNOWN':
        testsuite.lastRunResult = Result.UNKNOWN;
        break;
      case 'FAILED':
        testsuite.lastRunResult = Result.FAILED;
        break;
    }
    return testsuite;
  }

  constructor(private httpClient: HttpClient,
              private configurationService: ConfigurationService) {
  }

  getTestsuites(): Observable<Testsuite[]> {
    return this.httpClient.get<TestsuiteData[]>(this.baseUrl + '/testsuites/')
      .pipe(
        map(response => {
          const testsuites: Testsuite[] = new Array();
          response.forEach(element => {
            testsuites.push(TestsuiteService.mapResponseToTestsuite(element));
          });
          return testsuites;
        }));
  }

  getTestsuite(testsuiteId: string): Observable<Testsuite> {
    return this.httpClient.get<TestsuiteData>(this.baseUrl + '/testsuites/' + testsuiteId)
      .pipe(
        map(response =>
          TestsuiteService.mapResponseToTestsuite(response)
        ));
  }

  createTestsuite(testsuiteName: string): Observable<Testsuite> {
    const body = {name: testsuiteName};
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    return this.httpClient.post<Testsuite>(this.baseUrl + '/testsuites', body, httpOptions);
  }
}
