package no.bouvet.sandvika.activityboard.api;

import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.utils.ActivityUtils;
import no.bouvet.sandvika.activityboard.utils.LeaderboardUtils;
import no.bouvet.sandvika.activityboard.utils.StatsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ActivityController {
    @Autowired
    ActivityUtils activityUtils;

    @Autowired
    StatsUtils statsUtils;

    @Autowired
    LeaderboardUtils leaderboardUtils;


    @RequestMapping(value = "/activities/{clubName}/{activityType}/{periodType}", method = RequestMethod.GET)
    public List<Activity> getActivitiesForCurrentPeriod(@PathVariable("clubName") String clubName,
                                                        @PathVariable("activityType") String activityType,
                                                        @PathVariable("periodType") String periodType) {

        return activityUtils.getActivitiesForCurrentPeriodByActivityType(clubName, activityType.toLowerCase(), periodType);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/{periodType}/{periodNumber}/{year}", method = RequestMethod.GET)
    public List<Activity> getActivitiesForPeriod(@PathVariable("clubName") String clubName,
                                                 @PathVariable("activityType") String activityType,
                                                 @PathVariable("periodType") String periodType,
                                                 @PathVariable("periodNumber") int periodNumber,
                                                 @PathVariable("year") int year) {
        return activityUtils.getActivitiesForPeriodByActivityType(clubName, activityType.toLowerCase(), periodType, periodNumber, year);
    }

    @RequestMapping(value = "/leaderboard/{clubName}/history", method = RequestMethod.GET)
    public List<AthleteLeaderboardHistroy> getLeaderboardHistory(@PathVariable("clubName") String clubName) {
        return leaderboardUtils.getLeaderboardHistory(clubName);
    }

    @RequestMapping(value = "/leaderboard/{clubName}/{activityType}/{periodType}", method = RequestMethod.GET)
    public List<LeaderboardEntry> getLeaderboardForCurrentPeriod(@PathVariable("clubName") String clubName,
                                                                 @PathVariable("activityType") String activityType,
                                                                 @PathVariable("periodType") String periodType) {
        return leaderboardUtils.getLeaderboardEntries(clubName, activityType, periodType);
    }

    @RequestMapping(value = "/leaderboard/{clubName}/total/{date}", method = RequestMethod.GET)
    public List<LeaderboardEntry> getTotalLeaderboardOnDate(@PathVariable("clubName") String clubName,
                                                            @PathVariable("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        return leaderboardUtils.getLeaderboardEntries(clubName, date);
    }

    @RequestMapping(value = "/leaderboard/{clubName}/{activityType}/{periodType}/{periodNumber}/{year}", method = RequestMethod.GET)
    public List<LeaderboardEntry> getLeaderboardForPeriod(@PathVariable("clubName") String clubName,
                                                          @PathVariable("activityType") String activityType,
                                                          @PathVariable("periodType") String periodType,
                                                          @PathVariable("periodNumber") int periodNumber,
                                                          @PathVariable("year") int year) {
        return leaderboardUtils.getLeaderboardEntries(activityUtils.getActivities(clubName, activityType.toLowerCase(), periodType, periodNumber, year));
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/top/{limit}/{periodType}/{periodNumber}/{year}", method = RequestMethod.GET)
    public List<Activity> getTopActivitiesForPeriod(@PathVariable("clubName") String clubName,
                                                    @PathVariable("limit") int limit,
                                                    @PathVariable("activityType") String activityType,
                                                    @PathVariable("periodType") String periodType,
                                                    @PathVariable("periodNumber") int periodNumber,
                                                    @PathVariable("year") int year) {

        return activityUtils.getTopActivities(clubName, limit, activityType, periodType, periodNumber, year);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/top/{limit}/{periodType}", method = RequestMethod.GET)
    public List<Activity> getTopActivitiesForCurrentPeriod(@PathVariable("clubName") String clubName,
                                                           @PathVariable("limit") int limit,
                                                           @PathVariable("activityType") String activityType,
                                                           @PathVariable("periodType") String periodType) {

        return activityUtils.getTopActivitiesForCurrentPeriod(clubName, limit, activityType, periodType);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/stats/{periodType}", method = RequestMethod.GET)
    public Statistics getStatisticsForCurrentPeriodByActivityType(@PathVariable("clubName") String clubName,
                                                                  @PathVariable("activityType") String activityType,
                                                                  @PathVariable("periodType") String periodType) {
        return statsUtils.createStatsForCurrentPeriod(clubName, activityType.toLowerCase(), periodType);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/stats/{periodType}/{periodNumber}/{year}", method = RequestMethod.GET)
    public Statistics getStatisticsForPeriodByActivityType(@PathVariable("clubName") String clubName,
                                                           @PathVariable("activityType") String activityType,
                                                           @PathVariable("periodType") String periodType,
                                                           @PathVariable("periodNumber") int periodNumber,
                                                           @PathVariable("year") int year) {
        return statsUtils.createStatsForHistoricPeriod(clubName, activityType.toLowerCase(), periodType, periodNumber, year);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/latest/{numberOfActivities}", method = RequestMethod.GET)
    public List<Activity> getMostRecentActivitiesByActivityType(@PathVariable("clubName") String clubName,
                                                                @PathVariable("activityType") String activityType,
                                                                @PathVariable("numberOfActivities") int numberOfActivities) {
        return activityUtils.getMostRecentActivities(clubName, activityType, numberOfActivities);
    }

    @RequestMapping(value = "/activities/{activityType}/total/meters/{month}/{year}", method = RequestMethod.GET)
    public double getTotalMetersForMonthByActivity(@PathVariable("activityType") String activityType, @PathVariable("month") int month, @PathVariable("year") int year) {
        return activityUtils.getMetersForMonthByActivity(activityType, month, year);
    }

    @RequestMapping(value = "/activities/{clubName}/{activityType}/photos/latest/{numberOfPhotos}", method = RequestMethod.GET)
    public List<Photo> getRecentActivityPhotos(@PathVariable("clubName") String clubName,
                                               @PathVariable("activityType") String activityType,
                                               @PathVariable("numberOfPhotos") int numberOfPhotos) {

        return activityUtils.getMostRecentActivityPhotos(clubName, activityType.toLowerCase(), numberOfPhotos);
    }

    @RequestMapping(value = "/activities/{activityId}/pointcalculation", method = RequestMethod.GET)
    public PointsCalculation getPointsCalculationForActivity(@PathVariable("activityId") long activityId) {
        return activityUtils.getPointsCalculationForActivity(activityId);
    }
}
