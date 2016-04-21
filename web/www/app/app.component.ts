import {Component} from 'angular2/core';
import { HTTP_PROVIDERS } from 'angular2/http';
import {RouteConfig, ROUTER_DIRECTIVES, ROUTER_PROVIDERS} from 'angular2/router';
import {TestResultListComponent} from './test-result-list.component'
import {TestResultService} from './test-result.service'

@Component({
  selector: 'app',
  templateUrl: 'app/app.html',
  directives: [ROUTER_DIRECTIVES],
  providers: [
	  ROUTER_PROVIDERS,
    HTTP_PROVIDERS,
	  TestResultService
  ]
})
@RouteConfig([
{
	path: '/runs/:run_id/results',
	name: 'Results',
	component: TestResultListComponent
}
])
export class AppComponent {}