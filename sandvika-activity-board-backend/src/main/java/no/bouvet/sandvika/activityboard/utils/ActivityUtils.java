package no.bouvet.sandvika.activityboard.utils;

import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
public class ActivityUtils {

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    ClubRepository clubRepository;

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    public List<Activity> getActivitiesForCurrentPeriodByActivityType(String clubName, String activityType, String periodType) {
        return clubRepository.findById(clubName).map(club -> {
            Period period = DateUtil.getCurrentPeriod(PeriodType.valueOf(periodType.toUpperCase()), club.getCompetitionStartDate(), club.getCompetitionEndDate());
            return getActivitiesForPeriodByActivityType(clubName, activityType, period);
        }).orElse(new ArrayList<>());
    }

    public List<Activity> getActivitiesForPeriodByActivityType(String clubName, String activityType, String periodType, int periodNumber, int year) {
        Period period = DateUtil.getPeriod(PeriodType.valueOf(periodType.toUpperCase()), periodNumber, year);
        return getActivitiesForPeriodByActivityType(clubName, activityType, period);
    }

    public List<Activity> getActivitiesForPeriodByActivityType(String clubName, String activityType, Period period) {
        List<Activity> activityList;
        if (activityType.equalsIgnoreCase("all")) {
            activityList = activityRepository.findByStartDateLocalBetween(period.getStart(), period.getEnd());
        } else {
            activityList = activityRepository.findByStartDateLocalBetweenAndType(period.getStart(), period.getEnd(), activityType);
        }

        Club club = clubRepository.findById(clubName).orElse(null);

        return activityList.stream().filter(activity -> club != null && club.getMemberIds().contains(activity.getAthleteId())).collect(Collectors.toList());
    }

    public List<Activity> getUserActivitiesForPeriodByActivityType(int userId, String activityType, String periodType, int periodNumber, int year) {
        List<Activity> userActivities;

        userActivities = getUserActivitiesForPeriod(userId, periodType, periodNumber, year);

        if (Utils.hasValue(activityType) && !activityType.equalsIgnoreCase("all")) {
            userActivities = userActivities.stream().filter(activity -> activity.getType().equalsIgnoreCase(activityType)).collect(Collectors.toList());
        }

        return userActivities;
    }

    private List<Activity> getUserActivitiesForPeriod(int userId, String periodType, int periodNumber, int year) {
        List<Activity> userActivities;

        if (Utils.hasValue(periodType) && !periodType.equalsIgnoreCase("all")) {
            Period period = null;

            if ((periodNumber != 0) && (year != 0)) {
                period = DateUtil.getPeriod(PeriodType.valueOf(periodType.toUpperCase()), periodNumber, year);
            } else if (periodType.equalsIgnoreCase("week")) {
                period = DateUtil.getPeriodFromWeekStartToDate(DateUtil.getDateDaysAgo(1));
            } else if (periodType.equalsIgnoreCase("month")) {
                period = DateUtil.getPeriodFromMonthStartToDate(DateUtil.getDateDaysAgo(7));
            }

            List<Activity> activityList = activityRepository.findByStartDateLocalBetween(period.getStart(), period.getEnd());
            userActivities = activityList.stream().filter(activity -> activity.getAthleteId() == userId).collect(Collectors.toList());

        } else {
            userActivities = activityRepository.findByAthleteId(userId);
        }

        return userActivities;
    }

    public List<Activity> getActivities(String clubName,
                                        String activityType,
                                        String periodType,
                                        int periodNumber,
                                        int year) {
        return getActivitiesForPeriodByActivityType(clubName, activityType, periodType, periodNumber, year);
    }

    public List<Activity> getActivitiesByActivityType(String activityType, int numberOfActivities, String clubName) {
        return clubRepository.findById(clubName).map(club -> {
            return activityRepository.findByTypeAndStartDateLocalBetweenOrderByStartDateLocalDesc(club.getCompetitionStartDate(), club.getCompetitionEndDate(), activityType).
                    filter(activity -> club.getMemberIds().contains(activity.getAthleteId()))
                    .limit(numberOfActivities)
                    .collect(Collectors.toList());
        }).orElse(new ArrayList<>());
    }

    public List<Activity> getActivities(int numberOfActivities, String clubName) {
        return clubRepository.findById(clubName).map(club -> {
            return activityRepository.findByStartDateLocalBetweenOrderByStartDateLocalDesc(club.getCompetitionStartDate(), club.getCompetitionEndDate())
                    .filter(activity -> club.getMemberIds().contains(activity.getAthleteId()))
                    .limit(numberOfActivities)
                    .collect(Collectors.toList());
        }).orElse(new ArrayList<>());
    }

    public List<Activity> getTopActivities(String clubName, int limit, String activityType, String periodType, int periodNumber, int year) {
        return getActivitiesForPeriodByActivityType(clubName, activityType.toLowerCase(), periodType, periodNumber, year)
                .stream()
                .sorted(Comparator.comparingDouble(Activity::getPoints).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Activity> getTopActivitiesForCurrentPeriod(String clubName, int limit, String activityType, String periodType) {
        return getActivitiesForCurrentPeriodByActivityType(clubName, activityType.toLowerCase(), periodType)
                .stream()
                .sorted(Comparator.comparingDouble(Activity::getPoints).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public List<Activity> getMostRecentActivities(String clubName, String activityType, int numberOfActivities) {
        List<Activity> activityList;
        if (activityType.equalsIgnoreCase("all") || activityType.equalsIgnoreCase("")) {
            activityList = getActivities(numberOfActivities, clubName);
        } else {
            activityList = getActivitiesByActivityType(activityType.toLowerCase(), numberOfActivities, clubName);
        }
        return activityList;
    }

    public double getMetersForMonthByActivity(String activityType, int month, int year) {
        return activityRepository.findByStartDateLocalBetween(DateUtil.firstDayOfMonth(month - 1, year), DateUtil.lastDayOfMonth(month - 1, year))
                .stream()
                .filter(a -> a.getType().equalsIgnoreCase(activityType))
                .mapToDouble(Activity::getDistanceInMeters)
                .sum();
    }

    public List<Photo> getMostRecentActivityPhotos(String clubName, String activityType, int numberOfPhotos) {
        List<Photo> photoList;
        if (activityType.equalsIgnoreCase("all") || activityType.equalsIgnoreCase("")) {
            photoList = getPhotos(numberOfPhotos, clubName);
        } else {
            photoList = getPhotosByActivityType(clubName, numberOfPhotos, activityType.toLowerCase());
        }
        return photoList;
    }

    private List<Photo> getPhotosByActivityType(String clubName, int numberOfPhotos, String activityType) {
        return clubRepository.findById(clubName).map(club -> {
            List<Activity> activityList = activityRepository.findByTypeAndPhotosIsNotNullOrderByStartDateLocalDesc(activityType)
                    .filter(activity -> club.getMemberIds().contains(activity.getAthleteId()))
                    .limit(numberOfPhotos)
                    .collect(Collectors.toList());
            List<Photo> photos = new ArrayList<>();
            activityList.forEach(a -> photos.addAll(a.getPhotos()));

            return photos;
        }).orElse(new ArrayList<>());
    }

    private List<Photo> getPhotos(int numberOfPhotos, String clubName) {
        return clubRepository.findById(clubName).map(club -> {
            List<Activity> activityList = activityRepository.findAllByPhotosIsNotNullOrderByStartDateLocalDesc()
                    .filter(activity -> club.getMemberIds().contains(activity.getAthleteId()))
                    .limit(numberOfPhotos)
                    .collect(Collectors.toList());
            List<Photo> photos = new ArrayList<>();
            activityList.forEach(a -> photos.addAll(a.getPhotos()));

            return photos;
        }).orElse(new ArrayList<>());
    }

    public PointsCalculation getPointsCalculationForActivity(long activityId) {
        return activityRepository.findById(activityId).map(activity -> {
            PointsCalculation pointsCalculation = new PointsCalculation();
            pointsCalculation.setAchievements(activity.getAchievementCount());
            pointsCalculation.setElevationGain(activity.getTotalElevationGaininMeters());
            pointsCalculation.setHc(activity.getHandicap());
            pointsCalculation.setKm(activity.getDistanceInMeters() / 1000);
            pointsCalculation.setMinutes(activity.getMovingTimeInSeconds() / 60);
            pointsCalculation.setActivityType(activity.getType());
            pointsCalculation.setActivityId(activity.getId());
            pointsCalculation.setBadges(activity.getBadges());

            ActivityType activityType = ActivityType.toActivityType(activity.getType());
            pointsCalculation.setElevationCoeffisient(activityType.elevationCoefficient());
            pointsCalculation.setKmCoeffisient(activityType.distanceCoefficient());
            pointsCalculation.setMinCoeffisient(activityType.durationCoefficient());
            pointsCalculation.createCalculation();
            return pointsCalculation;
        }).orElse(null);
    }

    public List<String> getUserActivityTypesForPeriod(int athleteId, String periodType, int periodNumber, int year) {
        List<Activity> activityList = getUserActivitiesForPeriod(athleteId, periodType, periodNumber, year).stream()
                .filter(distinctByKey(Activity::getType))
                .collect(Collectors.toList());
        List<String> activityTypes = new ArrayList<>();
        activityList.forEach(a -> activityTypes.add(a.getType()));
        return activityTypes;
    }
}
