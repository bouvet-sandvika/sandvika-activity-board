package no.bouvet.sandvika.activityboard.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode
public class Badge {
    @Id
    private String name;
    private String type;
    private String uri;
    private String activityType;
    private float valueCriteria;
    private String timeCriteria;
    private String lessOrMore;
    private int minimumMinutes;
    private int minimumMeters;
    private int points;

    @Override
    public String toString() {
        return "Badge{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                ", activityType='" + activityType + '\'' +
                ", valueCriteria=" + valueCriteria +
                ", timeCriteria='" + timeCriteria + '\'' +
                ", lessOrMore='" + lessOrMore + '\'' +
                ", points=" + points +
                '}';
    }
}
