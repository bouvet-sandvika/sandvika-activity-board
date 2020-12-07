package no.bouvet.sandvika.activityboard.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import no.bouvet.sandvika.activityboard.points.PointsCalculator;

import java.util.Set;


@Getter
@Setter
@EqualsAndHashCode
public class PointsCalculation {
    private int minutes;
    private double elevationGain;
    private double km;
    private int achievements;
    private double hc;
    private String activityType;
    private long activityId;
    private double kmCoeffisient;
    private double minCoeffisient;
    private double elevationCoeffisient;
    private Set<Badge> badges;
    private String calculation;

    public void createCalculation() {
        String calc = "Points = (((" +
                minutes + " * " + PointsCalculator.MINUTE_VALUE + " * " + minCoeffisient +
                ") + (" +
                km + " * " + PointsCalculator.KILOMETER_VALUE + " * " + kmCoeffisient +
                ") + (" +
                elevationGain + " * " + PointsCalculator.ELEVATIOIN_METER_VALUE + " * " + elevationCoeffisient +
                ") + " +
                getPointsFromBadges() + "+" +
                achievements + ") * " + hc + ") /2" + " = " +
                (((minutes * minCoeffisient * PointsCalculator.MINUTE_VALUE) + (km * kmCoeffisient * PointsCalculator.KILOMETER_VALUE) + (elevationGain * elevationCoeffisient * PointsCalculator.ELEVATIOIN_METER_VALUE) + getPointsFromBadges() + achievements) * hc) / 2;
        calculation = calc;
    }

    private int getPointsFromBadges() {
        return this.badges.stream()
                .mapToInt(b -> b.getPoints())
                .sum();
    }

    private String printBadges() {
        String badgesNameAndPoints = "";
        for (Badge b : this.badges) {
            badgesNameAndPoints += b.getName() + ":" + b.getPoints();
        }
        return badgesNameAndPoints;
    }

    @Override
    public String toString() {
        return "PointsCalculation{" +
                "minutes=" + minutes +
                ", elevationGain=" + elevationGain +
                ", km=" + km +
                ", achievements=" + achievements +
                ", hc=" + hc +
                ", activityType='" + activityType + '\'' +
                ", activityId=" + activityId +
                ", kmCoeffisient=" + kmCoeffisient +
                ", minCoeffisient=" + minCoeffisient +
                ", elevationCoeffisient=" + elevationCoeffisient +
                ", badges: [" + printBadges() + "]" +
                '}';
    }


}
