export class HistoryEntry {
    revision: string;
    runId: string;
    results: {[id: string]: string};

    getShortRevision() {
        return this.revision.substring(0, 7);
    }

    getTestNames() {
        let testNames = [];
        for(var testName in this.results) {
            testNames.push(testName);
        }
        return testNames;
    }
}