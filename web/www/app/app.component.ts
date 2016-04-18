import {Component} from 'angular2/core';
import {TestResultsComponent} from './test-results.component'
import {TestResultService} from './test-result.service'

@Component({
  selector: 'app',
  template: `
  <p>Hello World</p>
  <test-results></test-results>
  `,
  directives: [TestResultsComponent],
  providers: [TestResultService]
})
export class AppComponent {}