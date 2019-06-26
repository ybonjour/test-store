import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';

@Pipe({ name: 'formatDate' })
export class FormatDatePipe implements PipeTransform {
  transform(dateString: string) {
    return moment(dateString).format('lll');
  }
}
