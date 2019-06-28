import { Component, OnInit } from '@angular/core';
import { I18nService } from '../../services/i18n/i18n.service';
import { NavigationService } from '../../services/navigation-service/navigation.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(private i18nService: I18nService,
              private navigationService: NavigationService) {
  }

  ngOnInit() {
  }

  setLanguage(language: string): void {
    this.i18nService.setLanguage(language);
  }

  backOption(): boolean {
    return this.navigationService.hasBackLink();
  }

  getLink(): string {
    return this.navigationService.getBackLink();
  }
}
