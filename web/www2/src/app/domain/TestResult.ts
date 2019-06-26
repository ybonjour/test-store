import { Result } from './Result';

export class TestResult {
  runId: string;
  result: Result;

  constructor(runId: string, result: Result) {
    this.runId = runId;
    this.result = result;
  }
}
