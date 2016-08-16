import {Pipe, PipeTransform} from "@angular/core";
import {HistoryTestName} from "./history-test-name";

@Pipe({ name: 'filter' })
export class HistoryFilter implements PipeTransform {
    transform(list: HistoryTestName[], applyFilter: string) {
        let applyFilterBool = applyFilter == 'true';
        if(!applyFilterBool) return list;
        return list.filter((item) => {return !item.allTestsPassed});
    }
}