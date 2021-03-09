package no.bouvet.sandvika.activityboard.points;

import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.domain.Club;
import no.bouvet.sandvika.activityboard.domain.HcClimbBoardEntry;
import no.bouvet.sandvika.activityboard.repository.ActivityRepository;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import no.bouvet.sandvika.activityboard.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HandicapClimbCalculator {

    private static Logger log = LoggerFactory.getLogger(HandicapClimbCalculator.class);
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    AthleteRepository athleteRepository;
    @Autowired
    ClubRepository clubRepository;
    @Autowired
    HandicapCalculator handicapCalculator;

    public List<HcClimbBoardEntry> calculateClimbForAllAthletes(Club club) {
        return club.getMemberIds()
                .stream()
                .map(i -> athleteRepository.findById(i.intValue()))
                .filter(a -> a.getToken() != null)
                .map(a -> calculateClimbForAthlete(a.getId(), club))
                .collect(Collectors.toList());
    }

    public HcClimbBoardEntry calculateClimbForAthlete(int athleteId, Club club) {
        Athlete athlete = athleteRepository.findById(athleteId);
        if (athlete == null) {

        }
        List<Double> handicapList = new ArrayList<>();
        double handicapClimb = 0;
        double previousHc = 0;
        double currentHc = 0;
        LocalDateTime start = LocalDateTime.ofInstant(club.getCompetitionStartDate().toInstant(), ZoneId.systemDefault());
        while (start.isBefore(LocalDateTime.now())) {
            previousHc = currentHc;
            currentHc = handicapCalculator.calculateHandicapForAthleteOnDate(athlete, DateUtil.getDateFromLocalDateTime(start));
            if (previousHc == 0) {
                continue;
            } else if (currentHc > previousHc) {
                handicapClimb += (currentHc - previousHc);
            }
            start = start.plusDays(1);
        }
        return createClimbEntry(athlete, handicapClimb);

    }

    private HcClimbBoardEntry createClimbEntry(Athlete athlete, double handicapClimb) {
        HcClimbBoardEntry entry = new HcClimbBoardEntry();
        entry.setAthleteFirstName(athlete.getFirstName());
        entry.setAthleteLastName(athlete.getLastName());
        entry.setAthleteId(athlete.getId());
        entry.setClimbNumber(handicapClimb);
        return entry;
    }

}
