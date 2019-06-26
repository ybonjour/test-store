import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Title } from '@angular/platform-browser';
import * as moment from 'moment';
import { ConfigurationService } from '../configuration-service/configuration.service';

@Injectable({
  providedIn: 'root'
})
export class I18nService {

  readonly defaultLanguage = this.configurationService.getDefaultLanguage();

  constructor(private translateService: TranslateService,
              private title: Title,
              private configurationService: ConfigurationService) {
  }

  setLanguage(language: string): void {
    moment.locale(language);
    this.translateService.use(language);
    this.translateService.get('app-name').subscribe(appTitle => this.title.setTitle(appTitle));
  }

  init(): void {
    moment.locale(this.defaultLanguage);
    this.translateService.setDefaultLang(this.defaultLanguage);
    this.setLanguage(this.defaultLanguage);
  }
}
