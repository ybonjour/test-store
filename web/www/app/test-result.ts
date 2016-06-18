export class TestResult {
	run: string;
	testName: string;
	retryNum: number;
	passed: boolean;
	durationMillis: number;
	stackTrace: string;
	stackTraceExpanded: boolean = false;
	showCollapseIcon: boolean = false;
	
	stackTraceExpandable() {
		return this.stackTrace != null && this.stackTrace.length > 3500;
	}

	toggleStackTraceExpanded() {
		this.stackTraceExpanded = !this.stackTraceExpanded;
		if(this.stackTraceExpanded) {
			this.showCollapseIcon = false;
		}
	}

	identifier() {
		return this.testName + "$" + this.retryNum;
	}
}