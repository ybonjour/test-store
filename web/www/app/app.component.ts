import {Component} from 'angular2/core';
import {TestResultsComponent} from './test-results.component'
@Component({
  selector: 'app',
  template: `
  <p>Hello World</p>
  <test-results></test-results>
  `,
  directives: [TestResultsComponent]
})
export class AppComponent {
}