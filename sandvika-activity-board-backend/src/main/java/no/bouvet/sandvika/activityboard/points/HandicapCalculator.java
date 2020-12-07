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

    private static final int NUM_DAYS_BACK_IN_TIME_TO_UPDATE_HC = 300;
    @Autowired
    ActivityRepository activityRepository;

    private static Logger log = LoggerFactory.getLogger(HandicapCalculator.class);

    private final AthleteRepository athleteRepository;
    private final ActiveHoursUtil activeHoursUtil;

    public HandicapCalculator(AthleteRepository athleteRepository, ActiveHoursUtil activeHoursUtil) {
        this.athleteRepository = athleteRepository;
        this.activeHoursUtil = activeHoursUtil;
    }

    @Scheduled(cron = "0 30 1 * * *")
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
        deleteHandicapsForAllAthlets(days);
        IntStream.range(0, days).forEach(i ->
                updateHandicapForAllAthletesForDate(DateUtil.getDateDaysAgo(i)));
    }

    public double getHandicapForActivity(Activity activity) {
        Athlete athlete = athleteRepository.findById(activity.getAthleteId()).orElse(null);
        if (athlete == null || athlete.getHandicapList().isEmpty()) {
            return 1;
        } else {
            return athlete.getHandicapForDate(activity.getStartDateLocal());
        }
    }

    private void deleteHandicapsForAllAthlets(int days) {
        List<Athlete> athletes = athleteRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -days);
        for (Athlete athlete : athletes) {
            athlete.setHandicapList(athlete.getHandicapList()
                    .stream()
                    .filter(h -> h.getTimestamp().before(calendar.getTime()))
                    .collect(Collectors.toList()));
        }
        athleteRepository.saveAll(athletes);
    }

    private void updateHandicapForAllAthletesForDate(Date date) {
        List<Athlete> athletes = athleteRepository.findAll();

        for (Athlete athlete : athletes) {
            Handicap hc = new Handicap(calculateHandicapForAthleteOnDate(athlete, date), date);
            athlete.getHandicapList().add(hc);
        }
        athleteRepository.saveAll(athletes);

    }

    protected double calculateHandicapForAthleteOnDate(Athlete athlete, Date date) {
        double activeHours = activeHoursUtil.getActiveHoursByDaysAndDateAndAthlete(30, date, athlete);
        return calculateHandicap(activeHours);
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
        log.info("\tHC: " + hc);
        return hc;
    }

    private void updateHandicapForAthleteForDate(int athleteId, Date dateDaysAgo) {
        Athlete athlete = athleteRepository.findById(athleteId);
        Handicap hc = new Handicap(calculateHandicapForAthleteOnDate(athlete, dateDaysAgo), dateDaysAgo);
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
