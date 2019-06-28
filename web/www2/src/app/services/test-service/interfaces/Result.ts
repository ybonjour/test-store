export interface Result {
  run: string;
  testName: string;
  retryNum: number;
  passed: boolean;
  durationMillis: number;
  time: string;
  stackTrace: string;
  log: string;
  failureReason: string;
  retry: boolean;
}
