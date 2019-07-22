import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor() {
  }

  getBaseUrl(): string {
    return '/api';
  }

  getDefaultLanguage(): string {
    return 'de';
  }
}
