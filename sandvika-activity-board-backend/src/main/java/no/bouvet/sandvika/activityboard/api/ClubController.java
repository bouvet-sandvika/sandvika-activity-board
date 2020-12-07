package no.bouvet.sandvika.activityboard.api;

import no.bouvet.sandvika.activityboard.domain.Athlete;
import no.bouvet.sandvika.activityboard.domain.Club;
import no.bouvet.sandvika.activityboard.repository.AthleteRepository;
import no.bouvet.sandvika.activityboard.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ClubController {
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    AthleteRepository athleteRepository;

    @RequestMapping(value = "/club", method = RequestMethod.POST)
    public void createClub(@RequestBody Club club) {
        if (club.getId() == null || club.getCompetitionStartDate() == null) {
            throw (new IllegalArgumentException());
        }

        for (int memberId : club.getMemberIds()) {
            if (!athleteRepository.existsById(memberId)) {
                createAthlete(memberId);
            }
        }

        club.getMemberIds().stream().map(athleteRepository::findById).forEach(athlete -> {
            athlete.ifPresent(athletePresent -> {
                if (!athletePresent.getClubs().contains(club.getId())) {
                    athletePresent.getClubs().add(club.getId());
                    athleteRepository.save(athletePresent);
                }
            });
        });

        clubRepository.save(club);
    }

    private void createAthlete(int memberId) {
        Athlete athlete = new Athlete();
        athlete.setId(memberId);
        athleteRepository.save(athlete);
    }

    @RequestMapping(value = "/club/{id}/{athleteId}", method = RequestMethod.PUT)
    public void addMember(@PathVariable("id") String id, @PathVariable("athleteId") int athleteId) {
        if (!clubRepository.existsById(id)) {
            throw new IllegalArgumentException("Club does not exist!");
        }

        clubRepository.findById(id).ifPresent(club -> {
            if (!athleteRepository.existsById(athleteId)) {
                createAthlete(athleteId);
            }
            club.addMember(athleteId);
            clubRepository.save(club);

            addClubToAthlete(athleteId, club);
        });
    }

    private void addClubToAthlete(@PathVariable("athleteId") int athleteId, Club club) {
        Athlete athlete = athleteRepository.findById(athleteId);
        if (!athlete.getClubs().contains(club.getId())) {
            athlete.getClubs().add(club.getId());
            athleteRepository.save(athlete);
        }
    }

    @RequestMapping(value = "/club/{id}/admin/{athleteId}", method = RequestMethod.PUT)
    public void addAdmin(@PathVariable("id") String id, @PathVariable("athleteId") int athleteId) {
        if (!clubRepository.existsById(id)) {
            throw new IllegalArgumentException("Club does not exist!");
        }

        clubRepository.findById(id).ifPresent(club -> {
            club.addAdmin(athleteId);
            clubRepository.save(club);

            addClubToAthlete(athleteId, club);
        });
    }

    @RequestMapping(value = "/club/athlete/{athleteId}", method = RequestMethod.GET)
    public List<Club> getAthleteClubs(@PathVariable("athleteId") int athleteId) {
        return clubRepository.findClubsByMemberIdsContains(athleteId);
    }

}
