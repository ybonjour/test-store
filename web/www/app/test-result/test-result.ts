export class TestResult {
	run: string;
	testName: string;
	retryNum: number;
	passed: boolean;
	durationMillis: number;
	time: Date;
	stackTrace: string;
}