import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatSortModule } from '@angular/material';

import { ChartsModule } from 'ng2-charts';

import { AppComponent } from './components/app.component';
import { AppRestService } from './service/app.rest.service';
import { NgbdModalComponent, NgbdModalContent } from './components/ngModal.component';
import { MeterToKilometerPipe } from './pipes/meterToKmPipe';
import {
  ConvertMinToHhMmSs,
  ConvertMinToHhT,
  ConvertNumberToMonth,
  ConvertNumberToMonthMinimal,
  TimeConverterPipe
} from './pipes/timeConverterPipe';
import { DecodeActivityTypePipe } from './pipes/decodeActivityTypePipe';
import { OrderBy } from './pipes/customOrderBy';
import { NgbdTabsetPills } from './components/nav.tabset.pills';
import { ActivityChartComponent } from './components/activityChart.component';
import { StatsDataComponent } from './components/stats.data.component';
import { LatestActivities } from './components/latest.activities.component';
import { TopActivities } from './components/top.activities.component';
import { OAuthModule } from 'angular-oauth2-oidc';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { PhotoCarouselComponent } from './photo-carousel/photo-carousel.component';
import { TitleCasePipe } from '@angular/common';
import { SortService } from './components/sort.service';

@NgModule({
  declarations: [
    AppComponent,
    NgbdModalComponent,
    NgbdModalContent,
    NgbdTabsetPills,
    ActivityChartComponent,
    StatsDataComponent,
    LatestActivities,
    TopActivities,
    MeterToKilometerPipe,
    TimeConverterPipe,
    DecodeActivityTypePipe,
    ConvertNumberToMonth,
    ConvertNumberToMonthMinimal,
    OrderBy,
    ConvertMinToHhMmSs,
    ConvertMinToHhT,
    PhotoCarouselComponent
  ],
  entryComponents: [NgbdModalContent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatSortModule,
    ChartsModule,
    FormsModule,
    HttpClientModule,
    OAuthModule.forRoot(),
    NgbModule.forRoot(),
    RouterModule.forRoot([{ path: '', component: AppComponent }])
  ],
  providers: [AppRestService, CookieService, SortService],
  bootstrap: [AppComponent]
})
export class AppModule {}
