import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Test } from '../../domain/Test';
import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';
import { TestData } from './interfaces/TestData';
import { ConfigurationService } from '../configuration-service/configuration.service';
import { Result } from '../../domain/Result';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  readonly baseUrl = this.configurationService.getBaseUrl();

  constructor(private httpClient: HttpClient,
              private configurationService: ConfigurationService) {
  }

  static mapResponseToResult(response: TestData): Test {
    const test = new Test();
    const result = response.results[0];

    test.name = result.testName;
    test.runId = result.run;
    test.retry = result.retry;
    test.retryNumber = result.retryNum;
    switch (response.testResult) {
      case 'PASSED':
        test.result = Result.SUCCESSFUL;
        break;
      case 'UNKNOWN':
        test.result = Result.UNKNOWN;
        break;
      case 'FAILED':
        test.result = Result.FAILED;
        break;
    }
    test.totalDurationMillis = result.durationMillis;
    test.dateTime = result.time;
    test.stackTrace = result.stackTrace;
    test.log = result.log;
    test.failureReason = result.failureReason;

    return test;
  }

  getResultsOverview(runId: string): Observable<Test[]> {
    return this.httpClient.get<TestData[]>(this.baseUrl + '/runs/' + runId + '/results')
      .pipe(
        map(response => {
          const testsOverview: Test[] = new Array();
          response.forEach(element => {
            testsOverview.push(TestService.mapResponseToResult(element));
          });
          return testsOverview;
        }));
  }
}
