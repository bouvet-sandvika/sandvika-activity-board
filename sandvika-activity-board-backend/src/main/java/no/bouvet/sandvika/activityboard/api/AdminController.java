package no.bouvet.sandvika.activityboard.api;

import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.points.BadgeAppointer;
import no.bouvet.sandvika.activityboard.points.HandicapCalculator;
import no.bouvet.sandvika.activityboard.points.PointsCalculator;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.BadgeRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import no.bouvet.sandvika.activityboard.strava.StravaSlurper;
import no.bouvet.sandvika.activityboard.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableAsync
public class AdminController {
    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminController.class);
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    BadgeRepository badgeRepository;
    @Autowired
    AthleteRepository athleteRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    StravaSlurper stravaSlurper;
    @Autowired
    HandicapCalculator handicapCalculator;

    @Autowired
    BadgeAppointer badgeAppointer;

    @RequestMapping(value = "/activities/refresh/{pages}", method = RequestMethod.GET)
    @ResponseBody
    public UpdateSummary refreshActivities(@PathVariable("pages") int pages) {
        return stravaSlurper.updateActivities(pages, 0);
    }

    @RequestMapping(value = "/activities/{id}", method = RequestMethod.PUT)
    public void addActivity(@PathVariable("id") long id, @RequestBody Activity activity) {
        activity.setHandicap(handicapCalculator.getHandicapForActivity(activity));
        activity.setPoints(PointsCalculator.getPointsForActivity(activity, activity.getHandicap()));
        activityRepository.save(activity);
    }

    @Async
    @RequestMapping(value = "/athlete/all/updateHistoricHandicap/{days}", method = RequestMethod.GET)
    public void updateHistoricHandicapForAllAthletes(@PathVariable("days") int days) {
        handicapCalculator.updateActivityHandicap(days);
    }

    @RequestMapping(value = "/athlete/{id}/activities/load/{pages}", method = RequestMethod.GET)
    @ResponseBody
    public Integer refreshActivitiesForAthlete(@PathVariable("id") int athleteId, @PathVariable("pages") int pages) {
        if (!athleteRepository.existsById(athleteId)) {
            throw new IllegalArgumentException("Athlete does not exist");
        }
        Athlete athlete = athleteRepository.findById(athleteId);

        return stravaSlurper.updateActivitiesForAthlete(pages, 0, athlete);

    }

    @RequestMapping(value = "/athlete/{id}/updateHistoricHandicap", method = RequestMethod.GET)
    public void updateHistoricHandicapForAthlete(@PathVariable("id") int id) {
        handicapCalculator.updateHandicapForAthlete(id);
        List<Activity> activities = activityRepository.findByAthleteId(id);
        for (Activity activity : activities) {
            activity.setHandicap(handicapCalculator.getHandicapForActivity(activity));
            activity.setPoints(PointsCalculator.getPointsForActivity(activity, activity.getHandicap()));
            activityRepository.save(activity);
        }
    }

    @RequestMapping(value = "/athlete", method = RequestMethod.POST)
    public void addAthlete(@RequestBody Athlete athlete) {
        athleteRepository.save(athlete);
    }

    @RequestMapping(value = "/athlete/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public String addToken(@PathVariable("id") int id, @RequestBody Token token) {
        Athlete athlete = athleteRepository.findById(id);
        if (athlete == null)
            return "Unknown user";
        athlete.setToken(token.getToken());
        athleteRepository.save(athlete);
        return "Token added to user " + athlete.toString();
    }

    @RequestMapping(value = "/activities/{id}", method = RequestMethod.DELETE)
    public void deleteActivity(@PathVariable("id") long id) {
        activityRepository.deleteById(id);
    }


    @RequestMapping(value = "/badges", method = RequestMethod.DELETE)
    public void deleteAllBadges() {
        badgeRepository.deleteAll();
    }

    @RequestMapping(value = "/badges", method = RequestMethod.POST)
    public void addBadge(@RequestBody Badge badge) {
        badgeRepository.save(badge);
    }

    @RequestMapping(value = "/badges", method = RequestMethod.GET)
    public List<Badge> listAllBadges() {
        return badgeRepository.findAll();
    }

    @RequestMapping(value = "/badges/appointHistoricalBadges/{days}", method = RequestMethod.GET)
    public void listAllBadges(@PathVariable("days") int days) {
        log.info("Appointing badges for the last " + days + " days.");
        List<Activity> activities = activityRepository.findByStartDateLocalAfter(DateUtil.getDateDaysAgo(days));
        log.info("Found " + activities.size() + " activities to check for badges.");
        activities.forEach(activity ->
        {
            activity.setBadges(badgeAppointer.getBadgesForActivity(activity));
            activityRepository.save(activity);
        });
    }

    @RequestMapping(value = "/badges/deleteAllBadgesFromAthletes", method = RequestMethod.DELETE)
    public void deleteAllBadgesFromAthletes() {
        List<Badge> badges = badgeRepository.findAll();
        badges.forEach(badge ->
        {
            //badge.setActivities(null);
            badgeRepository.save(badge);
        });

        List<Activity> activities = activityRepository.findAllByBadgesIsNotNull();
        activities.forEach(activity ->
        {
            activity.setBadges(null);
            activityRepository.save(activity);
        });

        List<Athlete> athletes = athleteRepository.findAllByBadgesIsNotNull();
        athletes.forEach(athlete ->
        {
            athlete.setBadges(null);
            athleteRepository.save(athlete);
        });

    }
}
