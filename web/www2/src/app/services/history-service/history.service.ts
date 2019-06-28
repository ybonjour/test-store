import { Injectable } from '@angular/core';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { RunHistories } from './interfaces/RunHistories';
import { TestHistories } from '../../domain/TestHistories';
import { ConfigurationService } from '../configuration-service/configuration.service';
import { TestHistory } from '../../domain/TestHistory';
import { RevisionResult } from '../../domain/RevisionResult';
import { TestResult } from '../../domain/TestResult';
import { Result } from '../../domain/Result';

@Injectable({
  providedIn: 'root'
})
export class HistoryService {

  constructor(private httpClient: HttpClient,
              private configurationService: ConfigurationService) {
  }

  readonly baseUrl = this.configurationService.getBaseUrl();

  static mapRunHistoriesToTestHistories(runHistories: RunHistories, testnames: string[]): TestHistories {
    const nameRegex = new RegExp('[^.]+$');
    const testHistories = new TestHistories();
    testHistories.revisions = [];
    testHistories.testHistories = [];

    testnames
      .forEach(testname => {
        const testHistory = new TestHistory();
        testHistory.revisionResults = [];
        testHistory.testName = testname.match(nameRegex)[0];
        testHistories.testHistories.push(testHistory);

      });

    runHistories.results
      .forEach(result => {
        if (!testHistories.revisions.includes(result.revision)) {
          testHistories.revisions.push(result.revision);
        }

        testHistories.testHistories
          .forEach(testHistory => {
            if (!testHistory.revisionResults.find(revisionResult => {
              return revisionResult.revisionId === result.revision;
            })) {
              testHistory.revisionResults.push(new RevisionResult(result.revision, []));
            }
          });

        for (let i = 0; i < result.results2.length; i++) {
          testHistories.testHistories[i].revisionResults
            .find(revisionResult => revisionResult.revisionId === result.revision).testResults
            .push(new TestResult(result.runId, this.mapStringToResult(result.results2[i])));
        }
      });

    this.setRevisionResults(testHistories.testHistories);
    return testHistories;
  }

  static mapStringToResult(resultString: string): Result {
    if (resultString === 'PASSED') {
      return Result.SUCCESSFUL;
    }
    if (resultString === 'UNKNOWN') {
      return Result.UNKNOWN;
    }
    if (resultString === 'FAILED') {
      return Result.FAILED;
    }
  }

  static setRevisionResults(testHistories: TestHistory[]) {
    testHistories.forEach(testHistory => {
      testHistory.revisionResults.forEach(revisionResult => {
        if (revisionResult.testResults.find(testResult => {
          return testResult.result === Result.FAILED;
        })) {
          if (revisionResult.testResults.find(testResult => {
            return testResult.result === Result.UNKNOWN || testResult.result === Result.SUCCESSFUL;
          })) {
            revisionResult.result = Result.PARTIALLY_FAILED;
          } else {
            revisionResult.result = Result.FAILED;
          }
        } else if (revisionResult.testResults.find(testResult => {
          return testResult.result === Result.UNKNOWN;
        })) {
          revisionResult.result = Result.UNKNOWN;
        } else {
          revisionResult.result = Result.SUCCESSFUL;
        }
      });
    });
  }

  getHistoryOfRuns(testsuiteId: string, testNames: string[]):
    Observable<TestHistories> {
    const body = testNames;
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
    return this.httpClient.post<RunHistories>(this.baseUrl + '/testsuites/' + testsuiteId + '/history/results', body, httpOptions)
      .pipe(
        map(response =>
          HistoryService.mapRunHistoriesToTestHistories(response, testNames)
        ));
  }

  getTestNames(testsuiteId
                 :
                 string
  ):
    Observable<string[]> {
    return this.httpClient.get<string[]>(this.baseUrl + '/testsuites/' + testsuiteId + '/history/testnames?limit=1&fetchSize=5');
  }
}
