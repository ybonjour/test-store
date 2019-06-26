import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DashboardComponent } from './screens/dashboard/dashboard.component';
import { TestsuiteCreateComponent } from './screens/testsuite-create/testsuite-create.component';
import { TestsuiteViewComponent } from './screens/testsuite-view/testsuite-view.component';
import { RunViewComponent } from './screens/run-view/run-view.component';
import { HistoryGridComponent } from './screens/history-grid/history-grid.component';

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'testsuite/create', component: TestsuiteCreateComponent },
  { path: 'testsuite/:testsuiteId', component: TestsuiteViewComponent },
  { path: 'testsuite/:testsuiteId/runs/:runId', component: RunViewComponent },
  { path: 'testsuite/:testsuiteId/history', component: HistoryGridComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
