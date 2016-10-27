import {Pipe, PipeTransform} from "@angular/core";
import {HistoryTestName} from "./history-test-name";

@Pipe({ name: 'historyFilterPassed' })
export class HistoryFilter implements PipeTransform {
    transform(list: HistoryTestName[], applyFilter: boolean) {
        if(!applyFilter) return list;
        return list.filter((item) => {return !item.allTestsPassed });
    }
}