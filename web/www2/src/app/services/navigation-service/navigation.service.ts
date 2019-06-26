import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NavigationService {

  private backLink: string;

  constructor() {
  }

  getBackLink(): string {
    return this.backLink;
  }

  setBackLink(path: string): string {
    return this.backLink = path;
  }

  hasBackLink(): boolean {
    return this.backLink != null;
  }
}
