import {TestResult} from "./test-result";

export class TestWithResults {
    testName: string;
    results: TestResult[];
    testResult: string;

    getRunId(): string {
        if(this.results == null || this.results.length == 0) {
            return null;
        }

        return this.results[0].run;
    }
}