package no.bouvet.sandvika.activityboard.strava;

import com.google.common.util.concurrent.RateLimiter;
import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.points.BadgeAppointer;
import no.bouvet.sandvika.activityboard.points.HandicapCalculator;
import no.bouvet.sandvika.activityboard.points.PointsCalculator;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import no.bouvet.sandvika.activityboard.utils.DateUtil;
import no.bouvet.sandvika.activityboard.utils.WeatherUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StravaSlurper {
    private static final String BASE_PATH = "https://www.strava.com/api/v3/";
    public static String STRAVA_CLIENT_SECERET = "506d1d0ed30af56b74a458a26419dd6ead8e910d";
    public static String  CLIENT_ID = "3034";
    private static Logger log = LoggerFactory.getLogger(StravaSlurper.class);
    final RateLimiter rateLimiter = RateLimiter.create(0.5); // rate is 0.5 permits per second"


    @Autowired
    RestTemplate restTemplateService;

    @Autowired
    ActivityRepository activityRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    HandicapCalculator handicapCalculator;

    @Autowired
    WeatherUtil weatherUtil;

    @Autowired
    BadgeAppointer badgeAppointer;

    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void updateLatestActivities() {
        updateActivities(1, DateUtil.getEpochDaysAgo(5));
    }

    @Scheduled(cron = "0 15 0 * * *")
    public void refreshActivities() {
        updateActivities(1, 0);
    }

    public UpdateSummary updateActivities(int pages, long after) {
        log.info("Updating activities");
        UpdateSummary updateSummary = new UpdateSummary();
        for (Athlete athlete : athleteRepository.findAllByTokenIsNotNull()) {
            Integer numberOfActivities = updateActivitiesForAthlete(pages, after, athlete);
            updateSummary.addNumberOfActivities(athlete.getLastName(), numberOfActivities);
        }
        return updateSummary;
    }

    public Integer updateActivitiesForAthlete(int pages, long after, Athlete athlete) {
        List<StravaActivity> stravaActivities = new ArrayList<>();

        IntStream.rangeClosed(1, pages).forEach(i -> stravaActivities.addAll(getActivitiesFromStrava(athlete, i, after)));
        List<Activity> activities = new ArrayList<>();
        stravaActivities.forEach(stravaActivity ->
                activities.add(createActivity(stravaActivity, athlete)));
        activityRepository.saveAll(activities
                .stream()
                .filter(activity -> activity.getPoints() > 0)
                .collect(Collectors.toList()));
        return activities.size();
    }


    private Activity createActivity(StravaActivity stravaActivity, Athlete athlete) {
        Activity activity = new Activity();
        activity.setAthleteFirstName(athlete.getFirstName());
        activity.setAthleteLastName(athlete.getLastName());
        activity.setAthleteId(athlete.getId());
        activity.setType(stravaActivity.getType());
        activity.setId(stravaActivity.getId());
        activity.setName(stravaActivity.getName());
        if (stravaActivity.getKilojoules() != null) {
            activity.setKiloJoules(stravaActivity.getKilojoules());
        }
        if (stravaActivity.getSufferScore() != null) {
            activity.setSufferScore(stravaActivity.getSufferScore());
        }
        activity.setElapsedTimeInSeconds(stravaActivity.getElapsedTime());
        if (stravaActivity.getAchievementCount() != null) {
            activity.setAchievementCount(stravaActivity.getAchievementCount());
        }
        if (stravaActivity.getTotalElevationGain() != null) {
            activity.setTotalElevationGaininMeters(stravaActivity.getTotalElevationGain());
        }
        activity.setMovingTimeInSeconds(stravaActivity.getMovingTime());
        activity.setDistanceInMeters(stravaActivity.getDistance());
        activity.setStartDateLocal(DateUtil.getDateFromLocalDateTimeString(stravaActivity.getStartDateLocal()));
        activity.setHandicap(handicapCalculator.getHandicapForActivity(activity));
        activity.setBadges(badgeAppointer.getBadgesForActivity(activity));
        activity.setPoints(PointsCalculator.getPointsForActivity(activity, handicapCalculator.getHandicapForActivity(activity)));
        if (stravaActivity.getTotalPhotoCount() > 0) {
            activity.setPhotos(getPhotosFromActivity(activity, athlete));
        }
        if (stravaActivity.getStartLatlng() != null) {
            activity.setStartLatLng(new double[]{stravaActivity.getStartLatlng().get(0), stravaActivity.getStartLatlng().get(1)});
            System.out.println("Preparing to set weather");
            setWeather(activity, stravaActivity);
        }
        log.debug("Created activity: " + activity.toString());
        return activity;
    }

    private void setWeather(Activity activity, StravaActivity stravaActivity) {
        Activity storedActivity = activityRepository.findById(activity.getId()).orElse(null);
        if (storedActivity == null) {
            activity.setWeather(weatherUtil.getWeatherForActivity(activity));
        } else if (storedActivity.getWeather() == null) {
            activity.setWeather(weatherUtil.getWeatherForActivity(activity));
        } else {
            activity.setWeather(storedActivity.getWeather());
        }
    }

    private List<Photo> getPhotosFromActivity(Activity activity, Athlete athlete) {
        StravaActivityFull stravaActivityFull = getActivityFromStrava(activity.getId(), athlete);
        return Arrays.asList(createPhoto(activity, stravaActivityFull));
    }

    private Photo createPhoto(Activity activity, StravaActivityFull stravaActivityFull) {
        return new Photo(activity.getSummary(), stravaActivityFull.getStravaPhotos().getStravaPrimaryPhoto().getStravaUrls().get600());

    }

    private StravaActivityFull getActivityFromStrava(long activityId, Athlete athlete) {
        if (athlete.getTokenExpires() == null || athlete.getTokenExpires().isBefore(Instant.now())) {
            refreshToken(athlete);
        }
        String url = BASE_PATH
                + "activities/" + activityId + "?access_token=" + athlete.getToken();
        rateLimiter.acquire();
        log.info(url);
        return restTemplateService.getForObject(url, StravaActivityFull.class);
    }

    protected List<StravaActivity> getActivitiesFromStrava(Athlete athlete, int page, long after) {
        if (athlete.getTokenExpires() == null || athlete.getTokenExpires().isBefore(Instant.now())) {
            refreshToken(athlete);
        }
        String url = BASE_PATH
                + "athlete/activities?" + (after == 0 ? "" : "after=" + after + "&") + "page=" + page + "&per_page=200&access_token=" + athlete.getToken();
        rateLimiter.acquire();
        log.info(url);
        StravaActivity[] activitiesFromStrava = null;
        try {
            activitiesFromStrava = restTemplateService.getForObject(url, StravaActivity[].class);
        } catch (RestClientException rce) {
            log.error("Could not get activities for " + athlete.getFirstName() + " " + athlete.getLastName() + " from Strava. Response from Strava: " + rce.getMessage());
        }
        if (activitiesFromStrava == null) {
            return new ArrayList<StravaActivity>();
        } else {
            return Arrays.asList(activitiesFromStrava);
        }
    }

    private void refreshToken(Athlete athlete) {
        String url = "https://www.strava.com/oauth/token?client_id=" + CLIENT_ID + "&client_secret=" + STRAVA_CLIENT_SECERET+"&grant_type=refresh_token&refresh_token=" + athlete.getRefreshToken();
        log.info(url);
        RefreshTokenResponse refreshTokenResponse = null;
        try {
            refreshTokenResponse = restTemplateService.postForObject(url, null, RefreshTokenResponse.class);
        } catch (RestClientException rce) {
            log.error("Could not retrieve new token for athlete " + athlete.getFirstName() + " " + athlete.getLastName() + ". Response from Strava " + rce.getMessage());
        }
        if (refreshTokenResponse != null) {
            athlete.setRefreshToken(refreshTokenResponse.getRefreshToken());
            athlete.setToken(refreshTokenResponse.getAccessToken());
            athlete.setTokenExpires(refreshTokenResponse.getExpiresAt());
            athleteRepository.save(athlete);
        }
    }


}



