package no.bouvet.sandvika.activityboard.utils;

import no.bouvet.sandvika.activityboard.domain.*;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LeaderboardUtils {

    @Autowired
    AthleteRepository athleteRepository;

    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ActivityUtils activityUtils;

    private static int getRankingByAthleteId(int athleteId, List<LeaderboardEntry> comparingLeaderboard) {
        return comparingLeaderboard
                .stream()
                .filter(l -> l.getAthleteId() == athleteId)
                .mapToInt(LeaderboardEntry::getRanking)
                .sum();
    }

    public List<LeaderboardEntry> addChangeToLeaderboard(List<LeaderboardEntry> currentLeaderboard, List<LeaderboardEntry> comparingLeaderboard) {
        currentLeaderboard.forEach(leaderboardEntry ->
        {
            int comparingRank = getRankingByAthleteId(leaderboardEntry.getAthleteId(), comparingLeaderboard);
            if (comparingRank == 0) {
                leaderboardEntry.setChange(99);
            } else {
                leaderboardEntry.setChange(
                        comparingRank - leaderboardEntry.getRanking());
            }
        });
        return currentLeaderboard;
    }

    public List<LeaderboardEntry> getLeaderboardEntries(List<Activity> activityList) {
        Map<Integer, LeaderboardEntry> entries = new HashMap<>();
        for (Activity activity : activityList) {
            if (!entries.containsKey(activity.getAthleteId())) {
                LeaderboardEntry entry = new LeaderboardEntry(activity.getAthleteId(), activity.getPoints());
                entry.setAthleteLastName(activity.getAthleteLastName());
                entry.setAthleteFirstName(activity.getAthleteFirstName());
                entry.setNumberOfActivities(1);

                entry.setHandicap(getCurrentHandicap(activity.getAthleteId()));
                entry.setKilometers(activity.getDistanceInMeters() / 1000);
                entry.setMinutes(activity.getMovingTimeInSeconds() / 60);
                entry.setLastActivityDate(activity.getStartDateLocal());
                entries.put(entry.getAthleteId(), entry);

            } else {
                LeaderboardEntry entry = entries.get(activity.getAthleteId());
                entry.setNumberOfActivities(entry.getNumberOfActivities() + 1);
                entry.setKilometers(entry.getKilometers() + (activity.getDistanceInMeters() / 1000));
                entry.setMinutes(Double.valueOf(entry.getMinutes() + (activity.getMovingTimeInSeconds() / 60)).intValue());
                entry.setPoints(entry.getPoints() + activity.getPoints());
                if (entry.getLastActivityDate().before(activity.getStartDateLocal())) {
                    entry.setLastActivityDate(activity.getStartDateLocal());
                }
            }
        }

        List<LeaderboardEntry> sortedEntries = (entries.values())
                .stream()
                .sorted(Comparator.comparingDouble(LeaderboardEntry::getPoints).reversed())
                .collect(Collectors.toList());

        for (LeaderboardEntry e : sortedEntries) {
            e.setRanking(sortedEntries.indexOf(e) + 1);
        }
        return sortedEntries;
    }


    private double getCurrentHandicap(int athleteId) {
        Athlete athlete = athleteRepository.findById(athleteId);
        if (athlete == null) {
            return 1;
        }
        return athlete.getHandicapForDate(new Date());
    }

    public List<LeaderboardEntry> getLeaderboardEntries(String clubName, String activityType, String periodType) {
        return clubRepository.findById(clubName).map(club -> {
            List<LeaderboardEntry> currentLeaderboard = getLeaderboardEntries(activityUtils.getActivitiesForCurrentPeriodByActivityType(clubName, activityType.toLowerCase(), periodType));
            Period comparingPeriod;
            if (periodType.equalsIgnoreCase("week")) {
                comparingPeriod = DateUtil.getPeriodFromWeekStartToDate(DateUtil.getDateDaysAgo(1));
            } else if (periodType.equalsIgnoreCase("month")) {
                comparingPeriod = DateUtil.getPeriodFromMonthStartToDate(DateUtil.getDateDaysAgo(7));
            } else {
                comparingPeriod = DateUtil.getPeriodBetweenDates(club.getCompetitionStartDate(), DateUtil.getDateDaysAgo(7));
            }
            List<LeaderboardEntry> comparingLeaderboard = getLeaderboardEntries(activityUtils.getActivitiesForPeriodByActivityType(clubName, "all", comparingPeriod));

            return addChangeToLeaderboard(currentLeaderboard, comparingLeaderboard);
        }).orElse(new ArrayList<>());
    }

    public List<LeaderboardEntry> getLeaderboardEntries(String clubName, Date inputDate) {
        return clubRepository.findById(clubName).map(club -> {
            Date calcEndDate = inputDate;
            if (calcEndDate.after(club.getCompetitionEndDate())) {
                calcEndDate = club.getCompetitionEndDate();
            }
            Period period = DateUtil.getPeriodBetweenDates(club.getCompetitionStartDate(), calcEndDate);
            List<LeaderboardEntry> currentLeaderboard = getLeaderboardEntries(activityUtils.getActivitiesForPeriodByActivityType(clubName, "all", period));
            Period comparingPeriod = DateUtil.getPeriodBetweenDates(club.getCompetitionStartDate(), DateUtil.firstDayOfCurrentMonth());
            List<LeaderboardEntry> comparingLeaderboard = getLeaderboardEntries(activityUtils.getActivitiesForPeriodByActivityType(clubName, "all", comparingPeriod));

            return addChangeToLeaderboard(currentLeaderboard, comparingLeaderboard);
        }).orElse(new ArrayList<>());
    }

    public int getLeaderboardStanding(String clubName, Date date, int athleteId) {
        return getLeaderboardEntries(clubName, date)
                .stream()
                .filter(l -> l.getAthleteId() == athleteId)
                .mapToInt(LeaderboardEntry::getRanking)
                .sum();
    }

    public List<AthleteLeaderboardHistroy> getLeaderboardHistory(String clubName) {
        Club club = clubRepository.findById(clubName).get();
        Date computationDate = club.getCompetitionStartDate();
        Date endDate = club.getCompetitionEndDate().after(new Date()) ? club.getCompetitionEndDate() : new Date();
        List<AthleteLeaderboardHistroy> athleteLeaderboardHistroy = new ArrayList<>();
        while (computationDate.before(endDate)) {
            List<LeaderboardEntry> leaderboard = getLeaderboardEntries(clubName, computationDate);
            AthleteLeaderboardHistroy alh = new AthleteLeaderboardHistroy(computationDate);
            for (LeaderboardEntry le : leaderboard) {
                alh.getRank().put(le.getAthleteFirstName() + " " + le.getAthleteLastName(), le.getRanking());
            }
            athleteLeaderboardHistroy.add(alh);
            computationDate = DateUtil.addDays(computationDate, 1);
        }
        return athleteLeaderboardHistroy;
    }
}

