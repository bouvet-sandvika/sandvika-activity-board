package no.bouvet.sandvika.activityboard.domain;

import java.util.Objects;

public class ActivityTypeSummary {
    private int numberOfActivities;
    private double numberOfMeters;
    private double numberOfMinutes;

    public int getNumberOfActivities() {
        return numberOfActivities;
    }

    public void setNumberOfActivities(int numberOfActivities) {
        this.numberOfActivities = numberOfActivities;
    }

    public double getNumberOfMeters() {
        return numberOfMeters;
    }

    public void setNumberOfMeters(double numberOfMeters) {
        this.numberOfMeters = numberOfMeters;
    }

    public double getNumberOfMinutes() {
        return numberOfMinutes;
    }

    public void setNumberOfMinutes(double numberOfMinutes) {
        this.numberOfMinutes = numberOfMinutes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityTypeSummary that = (ActivityTypeSummary) o;
        return numberOfActivities == that.numberOfActivities &&
                numberOfMeters == that.numberOfMeters &&
                numberOfMinutes == that.numberOfMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfActivities, numberOfMeters, numberOfMinutes);
    }
}
