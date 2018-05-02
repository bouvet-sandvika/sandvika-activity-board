package no.bouvet.sandvika.activityboard.points;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import no.bouvet.sandvika.activityboard.utils.ActiveHoursUtil;
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

    private static final int NUM_DAYS_BACK_IN_TIME_TO_UPDATE_HC = 100;
    @Autowired
    ActivityRepository activityRepository;

    private static Logger log = LoggerFactory.getLogger(HandicapCalculator.class);

    private final AthleteRepository athleteRepository;
    private final ActiveHoursUtil activeHoursUtil;

    public HandicapCalculator(AthleteRepository athleteRepository, ActiveHoursUtil activeHoursUtil) {
        this.athleteRepository = athleteRepository;
        this.activeHoursUtil = activeHoursUtil;
    }

    @Scheduled(cron = "0 0 1 * * *")
    private void updateActivityHandicapScheduledTask() {
        updateActivityHandicap(NUM_DAYS_BACK_IN_TIME_TO_UPDATE_HC);
    }

    public void updateActivityHandicap(int days) {
        updateHistoricalHandicapForAllAthletes(days);
        List<Activity> activities = activityRepository.findAll();
        for (Activity activity : activities) {
            activity.setHandicap(getHandicapForActivity(activity));
            activity.setPoints(PointsCalculator.getPointsForActivity(activity, activity.getHandicap()));
            activityRepository.save(activity);
        }
    }


    public void updateHistoricalHandicapForAllAthletes(int days) {
        deleteHandicapsForAllAthlets();
        IntStream.range(0, days).forEach(i ->
                updateHandicapForAllAthletesForDate(DateUtil.getDateDaysAgo(i)));
    }

    public double getHandicapForActivity(Activity activity) {
        Athlete athlete = athleteRepository.findById(activity.getAthleteId());
        if (athlete == null || athlete.getHandicapList().isEmpty()) {
            return 1;
        } else {
            return athlete.getHandicapForDate(activity.getStartDateLocal());
        }
    }

    private void deleteHandicapsForAllAthlets() {
        List<Athlete> athletes = athleteRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -2);
        for (Athlete athlete : athletes) {
            athlete.setHandicapList(athlete.getHandicapList()
                    .stream()
                    .filter(h -> h.getTimestamp().before(calendar.getTime()))
                    .collect(Collectors.toList()));
        }
        athleteRepository.save(athletes);
    }

    private void updateHandicapForAllAthletesForDate(Date date) {
        List<Athlete> athletes = athleteRepository.findAll();

        for (Athlete athlete : athletes) {
            Handicap hc = new Handicap(calculateHandicapForAthleteOnDate(athlete, date), date);
            log.info("Adding new HC for " + athlete.getLastName() + " " + hc.toString());
            athlete.getHandicapList().add(hc);
        }
        athleteRepository.save(athletes);

    }

    protected double calculateHandicapForAthleteOnDate(Athlete athlete, Date date) {
        double activeHours = activeHoursUtil.getActiveHoursByDaysAndDateAndAthlete(30, date, athlete);
        log.info("*** HC for " + athlete.getLastName() + " on " + date + " ***");
        log.info("\tActiveHours: " + activeHours);
        return calculateHandicap(activeHours);
    }

    private double calculateHandicap(double activeHours) {
        double rawHc = Utils.scaledDouble(20 * Math.pow(activeHours, -1), 3);
        log.info("\tRawHC: " + rawHc);
        double hc = 0;

        if (rawHc > 10 || rawHc == 0) {
            hc = 10;
        } else if (rawHc < 0.5) {
            hc = 0.5;
        } else {
            hc = rawHc;
        }
        log.info("\tHC: " + hc);
        return hc;
    }

    private void updateHandicapForAthleteForDate(int athleteId, Date dateDaysAgo) {
        Athlete athlete = athleteRepository.findById(athleteId);
        Handicap hc = new Handicap(calculateHandicapForAthleteOnDate(athlete, dateDaysAgo), dateDaysAgo);
        log.info("Adding new HC for " + athlete.getLastName() + " " + hc.toString());
        athlete.getHandicapList().add(hc);
        athleteRepository.save(athlete);
    }

    private void deleteHandicapsForAthleteTheLast300Days(int athleteId) {
        Athlete athlete = athleteRepository.findById(athleteId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -300);
        athlete.setHandicapList(athlete.getHandicapList()
                .stream()
                .filter(h -> h.getTimestamp().before(calendar.getTime()))
                .collect(Collectors.toList()));
        athleteRepository.save(athlete);
    }

    public void updateHandicapForAthlete(int athleteId) {
        deleteHandicapsForAthleteTheLast300Days(athleteId);
        IntStream.range(0, 999).forEach(i ->
                updateHandicapForAthleteForDate(athleteId, DateUtil.getDateDaysAgo(i)));
    }
}
