package no.bouvet.sandvika.activityboard.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AthleteLeaderboardHistroy {
   private Date date;
   private Map<String, Integer> rank;

    public AthleteLeaderboardHistroy(Date computationDate) {
        this.date = computationDate;
        this.rank = new HashMap<>();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<String, Integer> getRank() {
        return rank;
    }

    public void setRank(Map<String, Integer> rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AthleteLeaderboardHistroy that = (AthleteLeaderboardHistroy) o;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
