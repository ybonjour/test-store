export class TestStatistics {
    testSuite: string;
    testName: string;
    numPassed: number;
    numFailed: number;

    getTotalResults(): number {
        return this.numPassed + this.numFailed;
    }

    getPassRate(): number {
        let totalResults = this.getTotalResults()
        if(totalResults == 0) return 0;
        return this.numPassed / totalResults;
    }
}