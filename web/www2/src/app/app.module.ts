import { BrowserModule, Title } from '@angular/platform-browser';
import { ErrorHandler, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MaterialModule } from './framework/material/material.module';

import { TranslateLoader, TranslateModule, TranslateService } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { DashboardComponent } from './screens/dashboard/dashboard.component';
import { TestsuiteTileComponent } from './widgets/testsuite-tile/testsuite-tile.component';
import { HeaderComponent } from './widgets/header/header.component';
import { TestsuiteService } from './services/testsuite-service/testsuite.service';
import { TestsuiteCreateComponent } from './screens/testsuite-create/testsuite-create.component';
import { TestsuiteViewComponent } from './screens/testsuite-view/testsuite-view.component';
import { MatPaginatorIntl } from '@angular/material';
import { MatPaginatorIntlI18n } from './framework/material/MatPaginatorIntl';
import { HumanizeDatePipe } from './framework/pipes/date-time/humanize-date-pipe';
import { HumanizeDurationPipe } from './framework/pipes/date-time/humanize-duration-pipe';
import { FormatDatePipe } from './framework/pipes/date-time/format-date-pipe';
import { FormatDurationPipe } from './framework/pipes/date-time/format-duration-pipe';
import { GlobalErrorHandler } from './services/error-service/GlobalErrorHandler';
import { RunViewComponent } from './screens/run-view/run-view.component';
import { HistoryGridComponent } from './screens/history-grid/history-grid.component';


@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    TestsuiteTileComponent,
    HeaderComponent,
    TestsuiteCreateComponent,
    TestsuiteViewComponent,
    HumanizeDatePipe,
    HumanizeDurationPipe,
    FormatDatePipe,
    FormatDurationPipe,
    RunViewComponent,
    HistoryGridComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MaterialModule,
    HttpClientModule,
    FormsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
  ],
  providers: [
    Title,
    TestsuiteService,
    Location,
    {
      provide: MatPaginatorIntl,
      useFactory: (translate) => {
        const service = new MatPaginatorIntlI18n();
        service.injectTranslateService(translate);
        return service;
      },
      deps: [TranslateService]
    },
    {
      provide: ErrorHandler, useClass: GlobalErrorHandler
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
