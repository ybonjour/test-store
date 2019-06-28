import {Result} from './Result';

export class Run {
  id: string;
  testsuite: string;
  revision: string;
  dateTime: string;
  totalDurationMillis: string;
  result: Result;
  numberOfTestsTotal: number;
  numberOfTestsSuccessful: number;
  numberOfTestsFailed: number;
  device: string;

  isResultSuccessful(): boolean {
    return this.result === Result.SUCCESSFUL;
  }

  isResultFailed(): boolean {
    return this.result === Result.FAILED;
  }

  isResultUnknown(): boolean {
    return this.result === Result.UNKNOWN;
  }
}
