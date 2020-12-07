import {Component, OnDestroy, OnInit} from '@angular/core';
import {AppRestService} from '../service/app.rest.service';
import {ActivatedRoute} from '@angular/router';
import {Subscriber} from 'rxjs/Subscriber';
import {Athlete} from '../domain/athlete';
import {CookieService} from 'ngx-cookie-service';

@Component({
  selector: 'app-root',
  templateUrl: 'app.component.html',
  styleUrls: ['app.component.css'],
  providers: [AppRestService]
})
export class AppComponent implements OnInit, OnDestroy {
  private athlete: Athlete;
  private errorMessage: any;

  private redirectUrl: string;
  private defaultClub: string;

  constructor(private appRestService: AppRestService, private activatedRoute: ActivatedRoute, private cookie: CookieService) {
    this.appRestService.getUserInfo().subscribe(
      athlete => {
        this.athlete = athlete;
      },
      error => {
        (this.errorMessage = <any>error)
      })
  }

  ngOnInit() {
    if (window.location.port && window.location.port !== '80') {
      this.redirectUrl = window.location.protocol + '//' + window.location.hostname + ':' + window.location.port + window.location.pathname;
    } else {
      this.redirectUrl = window.location.protocol + '//' + window.location.hostname + window.location.pathname
    }

    if (this.cookie.check('default-club')) {
      this.defaultClub = this.cookie.get('default-club');
    }
  }

  ngOnDestroy() {
  }

  registerClub(club: string) {
    this.cookie.set('default-club', club);
    this.defaultClub = club;
  }

  resetClubEmitter(reset: boolean) {
    if (reset) {
      this.defaultClub = null;
    }
  }
}
