package no.bouvet.sandvika.activityboard.points;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import no.bouvet.sandvika.activityboard.utils.ActiveHoursUtil;
import no.bouvet.sandvika.activityboard.utils.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import no.bouvet.sandvika.activityboard.domain.Activity;
import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.domain.Handicap;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.utils.DateUtil;
import no.bouvet.sandvika.activityboard.utils.Utils;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class HandicapCalculator {

    private static final int NUM_DAYS_BACK_IN_TIME_TO_UPDATE_HC = 300;
    private static final int SECONDS_IN_HOUR = 3600;
    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    AthleteRepository athleteRepository;

    private static Logger log = LoggerFactory.getLogger(HandicapCalculator.class);

    private final ActiveHoursUtil activeHoursUtil;

    public HandicapCalculator(AthleteRepository athleteRepository, ActiveHoursUtil activeHoursUtil) {
        this.athleteRepository = athleteRepository;
        this.activeHoursUtil = activeHoursUtil;
    }

    @Scheduled(cron = "0 30 1 * * *")
    private void updateActivityHandicapScheduledTask() {
        updateActivityHandicap(NUM_DAYS_BACK_IN_TIME_TO_UPDATE_HC);
    }


//    @Scheduled(cron = "0 45 1 * * *")
    @Scheduled(fixedRate = 1000 * 60 * 10)
    private void setCurrentHandicap() {
        Iterable<Athlete> athletes = athleteRepository.findAllByUsernameIsNotNull();
        for (Athlete athlete: athletes) {
            Calendar start = Calendar.getInstance();
            start.setTime(DateUtil.getDateDaysAgo(30));
            Calendar end = Calendar.getInstance();
            List<Activity> activities = activityRepository.findByStartDateLocalBetweenAndAthleteId(start.getTime(), end.getTime(), athlete.getId());
            athlete.setCurrentHandicap(calculateHandicap(getActiveHoursInPeriod(start, end, activities)));
            athleteRepository.save(athlete);
        }
    }

    public void updateActivityHandicap(int days) {
        Iterable<Athlete> athletes = athleteRepository.findAllByUsernameIsNotNull();
        for (Athlete athlete : athletes) {
            updateHandicapForAthlete(athlete.getId());
        }
    }

    public void updateHandicapForAthlete(int athleteId) {
        Iterable<Activity> activities = activityRepository.findByAthleteId(athleteId);
        for (Activity activity : activities) {
            activity.setHandicap(getHandicapForActivity(activity));
            activity.setPoints(PointsCalculator.getPointsForActivity(activity, activity.getHandicap()));
            activityRepository.save(activity);
        }
    }

    public double getHandicapForActivity(Activity activity) {
        Calendar start = Calendar.getInstance();
        start.setTime(activity.getStartDateLocal());
        start.add(Calendar.DAY_OF_YEAR, -30);
        Calendar end = Calendar.getInstance();
        end.setTime(activity.getStartDateLocal());
        List<Activity> activities = activityRepository.findByStartDateLocalBetweenAndAthleteId(start.getTime(), end.getTime(), activity.getAthleteId());
        return calculateHandicap(getActiveHoursInPeriod(start, end, activities));
    }

    protected double calculateHandicapForAthleteOnDate(Athlete athlete, Date date) {
        double activeHours = activeHoursUtil.getActiveHoursByDaysAndDateAndAthlete(30, date, athlete);
        double hc = calculateHandicap(activeHours);
        log.debug(athlete.getLastName() + "," + date.toString() + ", " + hc);
        return hc;
    }

    private double calculateHandicap(double activeHours) {
        double rawHc = Utils.scaledDouble(20 * Math.pow(activeHours, -1), 3);
        double hc = 0;

        if (rawHc > 10 || rawHc == 0) {
            hc = 10;
        } else if (rawHc < 0.5) {
            hc = 0.5;
        } else {
            hc = rawHc;
        }
        return hc;
    }

    private int getActiveHoursInPeriod(Calendar start, Calendar end, List<Activity> activities) {
        return activities.stream()
                        .filter(a -> a.getStartDateLocal().before(end.getTime()) && a.getStartDateLocal().after(start.getTime()))
                        .mapToInt(Activity::getMovingTimeInSeconds)
                        .sum() / SECONDS_IN_HOUR;
    }
}
