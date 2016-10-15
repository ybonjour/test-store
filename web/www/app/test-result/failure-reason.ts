export class FailureReason {
    name: string;
    constructor(name: string){
        this.name = name;
    }

    static all(): FailureReason[] {
        let reasons = [];
        reasons.push(new FailureReason("Application Bug"));
        reasons.push(new FailureReason("Test Bug"));
        reasons.push(new FailureReason("Flaky"));

        return reasons;
    }
}