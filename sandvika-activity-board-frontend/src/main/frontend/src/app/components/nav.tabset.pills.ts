import {Component, OnInit} from "@angular/core";
import {AppRestService} from "../service/app.rest.service";
import {Athlete} from "../domain/athlete";
import {NgbTabChangeEvent} from "@ng-bootstrap/ng-bootstrap";
import {NgbDropdownConfig} from "@ng-bootstrap/ng-bootstrap";
import {TabContent} from "../domain/TabContent";
import {DateUtilsServiceService} from "../service/date-utils-service.service";
import {Statistics} from "../domain/Statistics";
import {Activity} from "../domain/activity";
import {ActivityType} from "../domain/ActivityType";

@Component({
    selector: 'ngbd-tabset-pills',
    templateUrl: './nav.tabset.pills.html',
    styleUrls: ['app.component.css'],
    providers: [AppRestService, DateUtilsServiceService, NgbDropdownConfig]
})
export class NgbdTabsetPills implements OnInit {
    private pageWeek: number;
    private pageMonth: number;
    private athletes: Athlete[];
    private pillTab: TabContent[] = [new TabContent("all", "Totalt", "competition"), new TabContent("month", "Måned", "month"), new TabContent("week", "Uke", "week")];

    private activities: Activity[];
    private latestActivities: Activity[];
    private activityTypes: ActivityType[] = [new ActivityType("All", "Alle"),
        new ActivityType("Run", "Løping"),
        new ActivityType("Ride", "Sykling"),
        new ActivityType("Swim", "Svømming"),
        new ActivityType("NordicSki", "Langrenn"),
        new ActivityType("Rowing", "Roing"),
        new ActivityType("Walk", "Gåing"),
        new ActivityType("Hike", "Hike"),
        new ActivityType("Workout", "Workout"),
        new ActivityType("WeightTraining", "Styrke"),
        new ActivityType("Kayaking", "Kayaking"),
        new ActivityType("RollerSki", "Rulleski"),
        new ActivityType("Yoga", "Yoga"),
        new ActivityType("EBikeRide", "El-sykkel")];

    private thisWeekStats: Statistics;
    private errorMessage: any;

    constructor(private appRestService: AppRestService) {
        this.pageWeek = DateUtilsServiceService.getWeekNumber(new Date());
        this.pageMonth = new Date().getMonth() + 1;

        this.pillTab[1].pageNumber = this.pageMonth;
        this.pillTab[2].pageNumber = this.pageWeek;

        this.pillTab[1].maxPage = this.pageMonth;
        this.pillTab[2].maxPage = this.pageWeek;
    }

    ngOnInit(): void {
        this.appRestService.getAllStats(this.pillTab[0]).subscribe(
            statistics => this.processStatsResult(statistics),
            error => this.errorMessage = <any>error
        );

        this.appRestService.getTopActivities(this.pillTab[0]).subscribe(
            activities => this.processTopResult(activities),
            error => this.errorMessage = <any>error
        );

        this.appRestService.getLeaderBoardTotalPoints(this.pillTab[0].activityType.code)
            .subscribe(
                athlete => this.athletes = athlete,
                error => this.errorMessage = <any>error);

        this.appRestService.getLatestActivities(this.pillTab[0].activityType.code, 5).subscribe(
            activities => this.processLatestResult(activities),
            error => this.errorMessage = <any>error
        );
    }

    public beforeChange($event: NgbTabChangeEvent) {
        let tab: TabContent = this.pillTab.find(value => value.code === $event.nextId);

        this.changeContent(tab, "none");
    };

    processLatestResult(result): void {
        this.latestActivities = result
    }

    processTopResult(result): void {
        this.activities = result
    }

    processStatsResult(result): void {
        this.thisWeekStats = result
    }

    private filter(tab: TabContent, activityType: ActivityType) {
        tab.activityType = activityType;
        this.changeContent(tab, "na")
    }

    private changeContent(tab: TabContent, state) {
        if (state === "prev") {
            tab.pageNumber--;
        } else if (state === "next") {
            tab.pageNumber++;
        }

        this.appRestService.getAllStats(tab).subscribe(
            statistics => this.processStatsResult(statistics),
            error => this.errorMessage = <any>error
        );

        this.appRestService.getTopActivities(tab).subscribe(
            activities => this.processTopResult(activities),
            error => this.errorMessage = <any>error
        );

        this.appRestService.getLatestActivities(tab.activityType.code, 5).subscribe(
            activities => this.processLatestResult(activities),
            error => this.errorMessage = <any>error
        );

        switch (tab.code) {
            case "all" :
                this.appRestService.getLeaderBoardTotalPoints(tab.activityType.code)
                    .subscribe(
                        athlete => this.athletes = athlete,
                        error => this.errorMessage = <any>error);
                break;
            case "month" :
                this.appRestService.getLeaderboardPoints(tab.activityType.code, "month", tab.pageNumber, tab.year)
                    .subscribe(
                        athlete => this.athletes = athlete,
                        error => this.errorMessage = <any>error);
                break;
            case "week" :
                this.appRestService.getLeaderboardPoints(tab.activityType.code, "week", tab.pageNumber, tab.year)
                    .subscribe(
                        athlete => this.athletes = athlete,
                        error => this.errorMessage = <any>error);

                this.appRestService.getAllStats(tab);

                break;
        }
    }
}
