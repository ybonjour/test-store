import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment';
import { duration } from 'moment';

@Pipe({ name: 'formatDuration' })
export class FormatDurationPipe implements PipeTransform {
  transform(timeMillis: string) {
    return moment.utc(duration(timeMillis).asMilliseconds()).format('HH:mm:ss');
  }
}
