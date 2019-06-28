import { TestResult } from './TestResult';
import { Result } from './Result';

export class RevisionResult {
  revisionId: string;
  result: Result;
  testResults: TestResult[];

  constructor(revisionId: string, testResults: TestResult[]) {
    this.revisionId = revisionId;
    this.testResults = testResults;
  }

  isResultSuccessful(): boolean {
    return this.result === Result.SUCCESSFUL;
  }

  isResultFailed(): boolean {
    return this.result === Result.FAILED;
  }

  isResultUnknown(): boolean {
    return this.result === Result.UNKNOWN;
  }

  isResultPartiallyFailed(): boolean {
    return this.result === Result.PARTIALLY_FAILED;
  }
}
