import { Component } from '@angular/core';
import { I18nService } from './services/i18n/i18n.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})


export class AppComponent {
  constructor(private i18nService: I18nService) {
    i18nService.init();
  }
}
