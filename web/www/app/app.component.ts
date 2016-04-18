import {Component} from 'angular2/core';
import {RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from 'angular2/router';
import {TestResultsComponent} from './test-results.component'
import {TestResultService} from './test-result.service'

@Component({
  selector: 'app',
  template: `
  	<h1>test-store</h1>
  	<a [routerLink]="['Results', {run_id: 'e7add2bc-a2f4-41ab-97ae-a2f210b3a447'}]">Results</a>
  	<router-outlet></router-outlet>
  `,
  directives: [ROUTER_DIRECTIVES],
  providers: [
	  ROUTER_PROVIDERS,
	  TestResultService
  ]
})
@RouteConfig([
{
	path: '/runs/:run_id/results',
	name: 'Results',
	component: TestResultsComponent
}
])
export class AppComponent {}