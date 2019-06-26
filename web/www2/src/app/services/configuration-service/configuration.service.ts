import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService {

  constructor() {
  }

  getBaseUrl(): string {
    return 'http://cazzati.ergon.ch:8099/api';
    // return 'http://localhost:8080';
  }

  getDefaultLanguage(): string {
    return 'de';
  }
}
