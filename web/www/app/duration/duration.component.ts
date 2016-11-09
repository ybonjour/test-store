import {Component, Input, OnInit} from "@angular/core";

@Component({
    selector: 'duration',
    templateUrl: 'app/duration/duration.html'
})
export class DurationComponent {
    @Input() durationMillis: number;

    printDuration() {
        var seconds = this.durationMillis / 1000;
        if(seconds == 0) return "0s";
        let hours = Math.floor(seconds / 3600);
        seconds = seconds % 3600;
        let minutes = Math.floor(seconds / 60);
        seconds = seconds % 60;

        return DurationComponent.printduration(hours, "h") + " " + DurationComponent.printduration(minutes, "m") + " " + DurationComponent.printduration(seconds, "s");
    }

    private static printduration(amount: number, unit: String): String {
        if(amount == 0) return "";
        else return "" + amount + unit;
    }
}