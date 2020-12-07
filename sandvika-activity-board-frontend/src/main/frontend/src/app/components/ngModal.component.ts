import {Component, Input, OnInit} from '@angular/core';
import {Sort} from '@angular/material';

import {NgbActiveModal, NgbModal, NgbModalOptions} from '@ng-bootstrap/ng-bootstrap';
import {AppRestService} from '../service/app.rest.service';
import {Activity} from '../domain/activity';
import {ModalAthlete} from '../domain/athlete';
import {SortService} from './sort.service';
import { TabContent } from '../domain/TabContent';

@Component({
    selector: 'ngbd-modal-component',
    template: '<a href="#" (click)="open()">{{selectedAthlete.athleteFirstName}} {{selectedAthlete.athleteLastName}}</a>',
    providers: [AppRestService],
    styleUrls: ['app.component.css']
})
export class NgbdModalComponent implements OnInit {
    @Input() selectedAthlete: ModalAthlete;
    @Input() activityType: string;
    @Input() periodType: string;
    @Input() periodNumber: number;
    @Input() year: number;

    private activities: Activity[];
    private errorMessage: any;

    constructor(private modalService: NgbModal, private appRestService: AppRestService) {}

    open() {
        const options: NgbModalOptions = {
            windowClass: 'modal-custom-size'
        };
        const modalRef = this.modalService.open(NgbdModalContent, options);
        modalRef.componentInstance.activities = this.activities;
        modalRef.componentInstance.selectedActivityType = this.activityType.toLowerCase();
        modalRef.componentInstance.athlete = this.selectedAthlete;
    }

    ngOnInit(): void {
      this.appRestService.getAthleteByActivitiyTypeOrPeriod(
        this.selectedAthlete.athleteId, this.activityType.toLowerCase(), this.periodType, this.periodNumber, this.year).subscribe(
        activity => this.activities = activity.slice(),
        error =>  this.errorMessage = <any>error);
      }
    }

@Component({
    selector: 'ngbd-modal-content',
    templateUrl: 'ngModalContent.component.html'
})
export class NgbdModalContent implements OnInit {
    @Input() activities;
    @Input() selectedActivityType;
    @Input() athlete;

    constructor(public activeModal: NgbActiveModal, private sortService: SortService) {}

    ngOnInit(): void {
      this.sortActivities({active: 'startDateLocal', direction: 'asc'});
    }

    sortActivities(sort: Sort): void {
      this.activities = this.sortService.sortActivities(sort, this.activities);
    }
}
