package no.bouvet.sandvika.activityboard.utils;

import no.bouvet.sandvika.activityboard.domain.Activity;
import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.domain.PeriodStats;
import no.bouvet.sandvika.activityboard.domain.PeriodType;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class ActiveHoursUtil {

    private final ActivityRepository activityRepository;

    private static final double SECONDS_IN_HOUR = 3600;

    public ActiveHoursUtil(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public double getActiveHoursByDaysAndDateAndAthlete(int days, Date dateDaysAgo, Athlete athlete)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateDaysAgo);
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        List<Activity> activities = activityRepository.findByStartDateLocalBetweenAndAthleteId(calendar.getTime(), dateDaysAgo, athlete.getId());
        return activities.stream()
                .mapToInt(Activity::getMovingTimeInSeconds)
                .sum() / SECONDS_IN_HOUR;
    }

    public double getActiveHoursByDaysAndAthlete(int days, Athlete athlete)
    {
        List<Activity> activities = activityRepository.findByStartDateLocalBetweenAndAthleteId(DateUtil.getDateDaysAgo(days), new Date(), athlete.getId());
        return  activities.stream()
                .mapToInt(Activity::getMovingTimeInSeconds)
                .sum() / SECONDS_IN_HOUR;
    }
}
