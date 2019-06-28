import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RunData } from './interfaces/RunData';
import { Run } from '../../domain/Run';
import { map } from 'rxjs/operators';
import { Result } from '../../domain/Result';
import { RunsOverview } from './interfaces/RunsOverview';
import { ConfigurationService } from '../configuration-service/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class RunService {

  constructor(private httpClient: HttpClient,
              private configurationService: ConfigurationService) {
  }

  readonly baseUrl = this.configurationService.getBaseUrl();

  static mapResponseToRun(response: RunData) {
    const run = new Run();
    run.id = response.run.id;
    run.testsuite = response.run.testSuite;
    run.revision = response.run.revision;
    run.dateTime = response.run.time;
    const tags: Map<string, string> = new Map<string, string>(Object.entries(response.run.tags));
    if (tags.has('device')) {
      run.device = tags.get('device');
    } else {
      run.device = null;
    }
    run.totalDurationMillis = response.runStatistics.totalDurationMillis;
    switch (response.runStatistics.result) {
      case 'PASSED':
        run.result = Result.SUCCESSFUL;
        break;
      case 'UNKNOWN':
        run.result = Result.UNKNOWN;
        break;
      case 'FAILED':
        run.result = Result.FAILED;
        break;
    }
    run.numberOfTestsSuccessful = response.runStatistics.numPassed;
    run.numberOfTestsFailed = response.runStatistics.numFailed;
    run.numberOfTestsTotal = response.runStatistics.numPassed + response.runStatistics.numFailed;
    return run;
  }

  getLastRun(testsuiteId: string): Observable<Run> {
    return this.httpClient.get<RunData>(this.baseUrl + '/testsuites/' + testsuiteId + '/runs/last')
      .pipe(
        map(response =>
          RunService.mapResponseToRun(response)
        ));
  }

  getRun(runId: string): Observable<Run> {
    return this.httpClient.get<RunData>(this.baseUrl + '/runs/' + runId)
      .pipe(
        map(response =>
          RunService.mapResponseToRun(response)
        ));
  }

  getRunsOverview(testsuiteId: string): Observable<Run[]> {
    return this.httpClient.get<RunsOverview>(this.baseUrl + '/testsuites/' + testsuiteId + '/runs/overview')
      .pipe(
        map(response => {
          const runsOverview: Run[] = new Array();
          response.results.forEach(element => {
            runsOverview.push(RunService.mapResponseToRun(element));
          });
          return runsOverview;
        }));
  }

}
