import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({ name: 'humanizeDate' })
export class HumanizeDatePipe implements PipeTransform {
  transform(dateString: string) {
    return moment(dateString).fromNow();
  }
}
