import { Result } from './Result';

export class Test {
  name: string;
  runId: string;
  retry: boolean;
  retryNumber: number;
  result: Result;
  totalDurationMillis: number;
  dateTime: string;
  stackTrace: string;
  log: string;
  failureReason: string;

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
