package no.bouvet.sandvika.activityboard.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"lastName", "id"})
public class Athlete {
    @Id
    private int id;
    private String username;
    @JsonProperty("lastname")
    private String lastName;
    @JsonProperty("firstname")
    private String firstName;
    private String profile;
    private String token;
    private Instant tokenExpires;
    private String refreshToken;
    private List<Handicap> handicapList = new ArrayList<>();
    private List<String> clubs = new ArrayList<>();
    private Map<String, List<Activity>> badges = new HashMap<>();

    public double getHandicapForDate(Date startDateLocal) {
        if (handicapList == null) {
            return 1;
        }
        OptionalDouble handicap = handicapList.stream()
                .filter(h -> h.getTimestamp().before(startDateLocal))
                .sorted(Comparator.comparing(Handicap::getTimestamp).reversed())
                .mapToDouble(Handicap::getHandicap)
                .findFirst();

        if (!handicap.isPresent()) {
            handicap = handicapList.stream()
                    .sorted(Comparator.comparing(Handicap::getTimestamp))
                    .mapToDouble(Handicap::getHandicap)
                    .findFirst();
        }

        if (!handicap.isPresent()) {
            return 1;
        }

        return handicap.getAsDouble();
    }

    public void addBadge(Badge badge, Activity activity) {
        Map<String, List<Activity>> badges = getBadges();
        if (badges.containsKey(badge)) {
            badges.get(badge).add(activity);
        } else {
            badges.put(badge.getName(), Arrays.asList(activity));
        }
    }

    public double getCurrentHandicap() {
        return getHandicapForDate(new Date());
    }
}
