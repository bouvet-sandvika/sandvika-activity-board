package no.bouvet.sandvika.activityboard.api;

import no.bouvet.sandvika.activityboard.domain.Activity;
import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.domain.AthleteStats;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.utils.ActiveHoursUtil;
import no.bouvet.sandvika.activityboard.utils.ActivityUtils;
import no.bouvet.sandvika.activityboard.utils.DateUtil;
import no.bouvet.sandvika.activityboard.utils.StatsUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AthleteController {

    private final ActivityRepository activityRepository;
    private final AthleteRepository athleteRepository;
    private final ActivityUtils activityUtils;
    private final ActiveHoursUtil activeHoursUtil;
    private final AdminController adminController;
    private final RestTemplate restTemplate;
    private final StatsUtils statsUtils;

    public AthleteController(ActivityRepository activityRepository, AthleteRepository athleteRepository, ActivityUtils activityUtils, ActiveHoursUtil activeHoursUtil, AdminController adminController, RestTemplate restTemplate, StatsUtils statsUtils) {
        this.activityRepository = activityRepository;
        this.athleteRepository = athleteRepository;
        this.activityUtils = activityUtils;
        this.activeHoursUtil = activeHoursUtil;
        this.adminController = adminController;
        this.restTemplate = restTemplate;
        this.statsUtils = statsUtils;
    }

    //@CrossOrigin("*")
    @RequestMapping(value = "/athlete/{id}/activities", method = RequestMethod.GET)
    public List<Activity> getUserActivities(@PathVariable("id") int id,
                                            @RequestParam("activityType") String activityType,
                                            @RequestParam("periodType") String periodType,
                                            @RequestParam("periodNumber") int periodNumber,
                                            @RequestParam("year") int year) {

        return activityUtils.getUserActivitiesForPeriodByActivityType(id, activityType, periodType, periodNumber, year);
    }

//    @RequestMapping(value = "/athlete/login", method = RequestMethod.GET)
////    @CrossOrigin(value = "*")
//    public Athlete tokenRegistration(@RequestParam String token) {
//        return athleteRepository.findOneByToken(token);
//    }
//
//    @RequestMapping(value = "/athlete/login/token-registration", method = RequestMethod.GET)
////    @CrossOrigin(value = "*")
//    public Athlete tokenRegistration(@RequestParam(required = false) String state, @RequestParam String code, HttpServletResponse response) {
//        StravaToken stravaToken = restTemplate.postForEntity("https://www.strava.com/oauth/token?client_id=3034&client_secret=506d1d0ed30af56b74a458a26419dd6ead8e910d&code=" + code, "", StravaToken.class).getBody();
//
//        Athlete athlete = null;
//        if (Objects.nonNull(stravaToken)) {
//            StravaAthlete stravaAthlete = stravaToken.getStravaAthlete();
//            athlete = athleteRepository.findById(stravaAthlete.getId());
//            if (athlete == null || athlete.getLastName() == null || athlete.getProfile() == null) {
//                if (athlete == null) {
//                    athlete = new Athlete();
//                }
//                athlete.setId(stravaAthlete.getId());
//                athlete.setFirstName(stravaAthlete.getFirstName());
//                athlete.setLastName(stravaAthlete.getLastName());
//                athlete.setProfile(stravaAthlete.getProfile());
//            }
//            athlete.setToken(stravaToken.getAccessToken());
//
//            athleteRepository.save(athlete);
//        } else {
//            throw new IllegalArgumentException("Strava token not found!");
//        }
//        if (athlete.getHandicapList().isEmpty()) {
//            adminController.updateHistoricHandicapForAthlete(athlete.getId());
//            adminController.refreshActivitiesForAthlete(athlete.getId(), 1);
//        }
//        return athlete;
//    }

    @RequestMapping(value = "/athlete", method = RequestMethod.GET)
    public List<Athlete> getAllAthletes() {
        return athleteRepository.findAll();
    }

    @RequestMapping(value = "/athlete/{id}", method = RequestMethod.GET)
    public Athlete getAthlete(@PathVariable("id") int id) {
        return athleteRepository.findById(id);
    }

    @RequestMapping(value = "/athlete/{id}/stats", method = RequestMethod.GET)
    public AthleteStats getAthleteStatistics(@PathVariable("id") int id) {
        return createAthleteStats(athleteRepository.findById(id), 0);
    }

    @RequestMapping(value = "/athlete/{id}/stats/{weeksBack}", method = RequestMethod.GET)
    public AthleteStats getAthleteStatisticsForWeeks(@PathVariable("id") int id, @PathVariable("weeksBack") int weeksBack) {
        return createAthleteStats(athleteRepository.findById(id), weeksBack);
    }

    @RequestMapping(value = "/athlete/stats", method = RequestMethod.GET)
    public List<AthleteStats> getAllAthleteStatistics() {
        return getAthleteStats(0);
    }

    @RequestMapping(value = "/athlete/stats/{weeksBack}", method = RequestMethod.GET)
    public List<AthleteStats> getAllAthleteStatisticsForWeeks(@PathVariable("weeksBack") int weeksBack) {
        return getAthleteStats(weeksBack);
    }

    private List<AthleteStats> getAthleteStats(int weeksBack) {
        List<Athlete> athletes = athleteRepository.findAll();
        List<AthleteStats> athleteStats = new ArrayList<>();
        for (Athlete athlete : athletes) {
            athleteStats.add(createAthleteStats(athlete, weeksBack));
        }
        return athleteStats;
    }

    /**
     * Henter statistikk for en utøver de siste ukene.
     *
     * @param athlete   utøveren man skal finne statistikk for.
     * @param weeksBack hvor mange uker man skal gå tilbake. Dersom 0 tar man for inneværende år
     * @return AthleteStats
     */

    private AthleteStats createAthleteStats(Athlete athlete, int weeksBack) {
        AthleteStats athleteStats = new AthleteStats();
        athleteStats.setId(athlete.getId());
        athleteStats.setName(athlete.getFirstName() + " " + athlete.getLastName());
        athleteStats.setHc(athlete.getCurrentHandicap());
        athleteStats.setActiveHoursThisYear(activeHoursUtil.getActiveHoursByDaysAndAthlete(DateUtil.getDayOfCurrentYear(), athlete));
        athleteStats.setActiveHoursThisMonth(activeHoursUtil.getActiveHoursByDaysAndAthlete(DateUtil.getDaysSinceDate(DateUtil.firstDayOfCurrentMonth()), athlete));
        athleteStats.setActiveHoursThisWeek(activeHoursUtil.getActiveHoursByDaysAndAthlete(DateUtil.getDaysSinceDate(DateUtil.firstDayOfCurrentWeek()), athlete));
        athleteStats.setActiveHoursHcPeriod(activeHoursUtil.getActiveHoursByDaysAndAthlete(30, athlete));
        if (weeksBack == 0) {
            athleteStats.setWeeklyStats(statsUtils.getWeeklyStatsForYear(athlete.getId()));
            athleteStats.setMonthlyStats(statsUtils.getMonthlyStatsForYear(athlete.getId()));
        } else {
            athleteStats.setWeeklyStats(statsUtils.getWeeklyStats(athlete.getId(), weeksBack));
            athleteStats.setMonthlyStats(statsUtils.getMonthlyStats(athlete.getId(), weeksBack));
        }
        return athleteStats;
    }
}
