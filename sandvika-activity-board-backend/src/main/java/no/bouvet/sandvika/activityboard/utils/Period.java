package no.bouvet.sandvika.activityboard.utils;

import java.util.Date;

public class Period {
    private Date start;
    private Date end;
    private int periodNumber;
    private int year;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getPeriodNumber() {
        return periodNumber;
    }

    public void setPeriodNumber(int periodNumber) {
        this.periodNumber = periodNumber;
    }

    public int getDaysSinceStart() {
        if (start == null) {
            return 0;
        } else {
            return (int) Math.round((new Date().getTime() - start.getTime()) / (double) 86400000);
        }
    }
}
