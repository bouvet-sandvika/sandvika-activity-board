package no.bouvet.sandvika.activityboard.domain;

import java.util.List;

public class AthleteStats {
    private String name;
    private int id;
    private double hc;
    private double activeHoursThisWeek;
    private double activeHoursThisMonth;
    private double activeHoursThisYear;
    private double activeHoursHcPeriod;
    private List<PeriodStats> weeklyStats;
    private List<PeriodStats> monthlyStats;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getHc() {
        return hc;
    }

    public void setHc(double hc) {
        this.hc = hc;
    }

    public double getActiveHoursThisWeek() {
        return activeHoursThisWeek;
    }

    public void setActiveHoursThisWeek(double activeHoursThisWeek) {
        this.activeHoursThisWeek = activeHoursThisWeek;
    }

    public double getActiveHoursThisMonth() {
        return activeHoursThisMonth;
    }

    public void setActiveHoursThisMonth(double activeHoursThisMonth) {
        this.activeHoursThisMonth = activeHoursThisMonth;
    }

    public double getActiveHoursThisYear() {
        return activeHoursThisYear;
    }

    public void setActiveHoursThisYear(double activeHoursThisYear) {
        this.activeHoursThisYear = activeHoursThisYear;
    }

    public double getActiveHoursHcPeriod() {
        return activeHoursHcPeriod;
    }

    public void setActiveHoursHcPeriod(double activeHoursHcPeriod) {
        this.activeHoursHcPeriod = activeHoursHcPeriod;
    }

    public List<PeriodStats> getWeeklyStats() {
        return weeklyStats;
    }

    public void setWeeklyStats(List<PeriodStats> weeklyStats) {
        this.weeklyStats = weeklyStats;
    }

    public List<PeriodStats> getMonthlyStats() {
        return monthlyStats;
    }

    public void setMonthlyStats(List<PeriodStats> monthlyStats) {
        this.monthlyStats = monthlyStats;
    }
}
