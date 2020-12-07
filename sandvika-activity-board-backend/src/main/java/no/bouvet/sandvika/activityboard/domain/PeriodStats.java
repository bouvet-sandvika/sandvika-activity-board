package no.bouvet.sandvika.activityboard.domain;

import java.util.HashMap;
import java.util.Objects;

public class PeriodStats {
    private PeriodType periodType;
    private int periodNumber;
    private int year;
    private int athlete;
    private double hcStartPeriod;
    private double hcEndPeriod;
    private HashMap<String, Integer> boardStandingStartPeriod;
    private HashMap<String, Integer> boardStandingEndPeriod;
    private HashMap<String, ActivityTypeSummary> activtyTypeSummary;

    public PeriodType getPeriodType() {
        return periodType;
    }

    public void setPeriodType(PeriodType periodType) {
        this.periodType = periodType;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getAthlete() {
        return athlete;
    }

    public void setAthlete(int athlete) {
        this.athlete = athlete;
    }

    public double getHcStartPeriod() {
        return hcStartPeriod;
    }

    public void setHcStartPeriod(double hcStartPeriod) {
        this.hcStartPeriod = hcStartPeriod;
    }

    public double getHcEndPeriod() {
        return hcEndPeriod;
    }

    public void setHcEndPeriod(double hcEndPeriod) {
        this.hcEndPeriod = hcEndPeriod;
    }

    public HashMap<String, Integer> getBoardStandingStartPeriod() {
        return boardStandingStartPeriod;
    }

    public void setBoardStandingStartPeriod(HashMap<String, Integer> boardStandingStartPeriod) {
        this.boardStandingStartPeriod = boardStandingStartPeriod;
    }

    public HashMap<String, Integer> getBoardStandingEndPeriod() {
        return boardStandingEndPeriod;
    }

    public void setBoardStandingEndPeriod(HashMap<String, Integer> boardStandingEndPeriod) {
        this.boardStandingEndPeriod = boardStandingEndPeriod;
    }

    public HashMap<String, ActivityTypeSummary> getActivtyTypeSummary() {
        return activtyTypeSummary;
    }

    public void setActivtyTypeSummary(HashMap<String, ActivityTypeSummary> activtyTypeSummary) {
        this.activtyTypeSummary = activtyTypeSummary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeriodStats that = (PeriodStats) o;
        return periodNumber == that.periodNumber &&
                year == that.year &&
                athlete == that.athlete &&
                boardStandingStartPeriod == that.boardStandingStartPeriod &&
                boardStandingEndPeriod == that.boardStandingEndPeriod &&
                periodType == that.periodType &&
                Objects.equals(activtyTypeSummary, that.activtyTypeSummary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(periodType, periodNumber, year, athlete, hcStartPeriod, hcEndPeriod, boardStandingStartPeriod, boardStandingEndPeriod, activtyTypeSummary);
    }
}
