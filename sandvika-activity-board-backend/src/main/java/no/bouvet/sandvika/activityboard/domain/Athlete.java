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
    private double currentHandicap;
    private List<String> clubs = new ArrayList<>();
    private Map<String, List<Activity>> badges = new HashMap<>();

    public void addBadge(Badge badge, Activity activity) {
        Map<String, List<Activity>> badges = getBadges();
        if (badges.containsKey(badge)) {
            badges.get(badge).add(activity);
        } else {
            badges.put(badge.getName(), Arrays.asList(activity));
        }
    }
}
