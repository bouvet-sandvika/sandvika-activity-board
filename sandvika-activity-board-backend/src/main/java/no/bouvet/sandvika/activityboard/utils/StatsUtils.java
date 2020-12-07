package no.bouvet.sandvika.activityboard.utils;

import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@Component
public class StatsUtils {
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    ActivityUtils activityUtils;

    @Autowired
    LeaderboardUtils leaderboardUtils;

    public Statistics createStatsForCurrentPeriod(String clubName, String activityType, String periodType) {
        return clubRepository.findById(clubName).map(club -> {
            Period period = DateUtil.getCurrentPeriod(PeriodType.valueOf(periodType.toUpperCase()), club.getCompetitionStartDate(), club.getCompetitionEndDate());
            return getStatistics(clubName, activityType, periodType, period);
        }).orElse(new Statistics());
    }

    public Statistics createStatsForHistoricPeriod(String clubName, String activityType, String periodType, int periodNumber, int year) {
        Period period = DateUtil.getPeriod(PeriodType.valueOf(periodType.toUpperCase()), periodNumber, year);

        return getStatistics(clubName, activityType, periodType, period);
    }

    private Statistics getStatistics(String clubName, String activityType, String periodType, Period period) {
        List<Activity> activities;
        activities = activityUtils.getActivitiesForPeriodByActivityType(clubName, activityType, period);
        Statistics stats = new Statistics();
        stats.setType(activityType);
        stats.setPeriodType(PeriodType.valueOf(periodType.toUpperCase()));
        stats.setStartDate(period.getStart());
        stats.setAchievements(activities.stream().mapToInt(Activity::getAchievementCount).sum());
        stats.setMeters(Utils.scaledDouble(activities.stream().mapToDouble(Activity::getDistanceInMeters).sum()));
        stats.setMinutes(Utils.scaledDouble(activities.stream().mapToDouble(Activity::getMovingTimeInSeconds).sum() / 60));
        stats.setActivities(activities.size());
        stats.setKiloJoules(activities.stream().mapToDouble(Activity::getKiloJoules).sum());
        return stats;
    }

    public PeriodStats getAthleteStatsForPeriod(PeriodType periodType, int periodNumber, int year, int athleteId) {
        List<String> activityTypes = activityUtils.getUserActivityTypesForPeriod(athleteId, periodType.name(), periodNumber, year);
        HashMap<String, ActivityTypeSummary> activityTypeSummaries = new HashMap<>();
        for (String activityType : activityTypes) {
            List<Activity> activities = activityUtils.getUserActivitiesForPeriodByActivityType(athleteId, activityType, periodType.name(), periodNumber, year);
            activityTypeSummaries.put(activityType, createActivtyTypeSummary(activities));
        }
        PeriodStats periodStats = new PeriodStats();
        periodStats.setActivtyTypeSummary(activityTypeSummaries);
        periodStats.setAthlete(athleteId);
        periodStats.setPeriodNumber(periodNumber);
        periodStats.setPeriodType(periodType);
        periodStats.setYear(year);
        Period period = DateUtil.getPeriod(periodType, periodNumber, year);
        Athlete athlete = athleteRepository.findById(athleteId);
        HashMap<String, Integer> leaderboardAtStart = new HashMap<>();
        HashMap<String, Integer> leaderboardAtEnd = new HashMap<>();
        for (String club : athlete.getClubs()) {
            leaderboardAtEnd.put(club, leaderboardUtils.getLeaderboardStanding(club, period.getEnd(), athleteId));
            leaderboardAtStart.put(club, leaderboardUtils.getLeaderboardStanding(club, period.getStart(), athleteId));
        }
        periodStats.setBoardStandingEndPeriod(leaderboardAtEnd);
        periodStats.setBoardStandingStartPeriod(leaderboardAtStart);
        periodStats.setHcStartPeriod(athlete.getHandicapForDate(period.getStart()));
        periodStats.setHcEndPeriod(athlete.getHandicapForDate(period.getEnd()));
        return periodStats;
    }

    private ActivityTypeSummary createActivtyTypeSummary(List<Activity> activities) {
        ActivityTypeSummary summary = new ActivityTypeSummary();
        summary.setNumberOfActivities(activities.size());
        summary.setNumberOfMeters(Utils.scaledDouble(activities.stream().mapToDouble(Activity::getDistanceInMeters).sum()));
        summary.setNumberOfMinutes(Utils.scaledDouble(activities.stream().mapToDouble(Activity::getMovingTimeInSeconds).sum() / 60));
        return summary;
    }

    public List<PeriodStats> getWeeklyStatsForYear(int athleteId) {
        Period currentPeriod = DateUtil.getCurrentPeriod(PeriodType.WEEK, null, null);
        List<PeriodStats> periodStats = new ArrayList<>();
        for (int i = 1 ; i <= currentPeriod.getPeriodNumber() ; i++) {
            periodStats.add(getAthleteStatsForPeriod(PeriodType.WEEK, i, currentPeriod.getYear(), athleteId));
        }
        return periodStats;
    }

    public List<PeriodStats> getWeeklyStats(int athleteId, int weeksBack) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, -weeksBack);
        List<PeriodStats> periodStats = new ArrayList<>();
        for (int i = 0; i <= weeksBack; i++) {
            periodStats.add(getAthleteStatsForPeriod(PeriodType.WEEK, cal.get(Calendar.WEEK_OF_YEAR), cal.get(Calendar.YEAR), athleteId));
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }
        return periodStats;
    }

    public List<PeriodStats> getMonthlyStatsForYear(int athleteId) {
        Period currentPeriod = DateUtil.getCurrentPeriod(PeriodType.MONTH, null, null);
        List<PeriodStats> periodStats = new ArrayList<>();
        for (int i = 1 ; i <= currentPeriod.getPeriodNumber() ; i++) {
            periodStats.add(getAthleteStatsForPeriod(PeriodType.MONTH, i, currentPeriod.getYear(), athleteId));
        }
        return periodStats;
    }

    public List<PeriodStats> getMonthlyStats(int athleteId, int weeksBack) {
        Calendar runningDate = Calendar.getInstance();
        runningDate.add(Calendar.WEEK_OF_YEAR, -weeksBack);
        Calendar currentDate = Calendar.getInstance();
        List<PeriodStats> periodStats = new ArrayList<>();
        while (runningDate.before(currentDate)) {
            periodStats.add(getAthleteStatsForPeriod(PeriodType.MONTH, runningDate.get(Calendar.MONTH ) + 1 , runningDate.get(Calendar.YEAR), athleteId));
            runningDate.add(Calendar.MONTH, 1);
        }
        return periodStats;
    }
}
