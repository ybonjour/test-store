export class HistoryTestName {
    longName: string;
    shortName: string;
    allTestsPassed = true;

    private static maxLengthShort: number = 50;

    constructor(longName:string){
        this.longName = longName;
        this.shortName = HistoryTestName.shorten(longName);
    }

    addResult(result: String){
        this.allTestsPassed = this.allTestsPassed && (result == 'PASSED' || result == 'UNKNOWN');
    }

    private static shorten(name: String) {
        let tokens = name.split('.');
        let shortName = tokens[tokens.length - 1];
        if(shortName.length < HistoryTestName.maxLengthShort) return shortName;
        else return shortName.substr(0, HistoryTestName.maxLengthShort - 3) + "...";
    }

}