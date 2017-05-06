package no.bouvet.sandvika.activityboard.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.springframework.data.annotation.Id;

public class Athlete
{

    @Id
    private int id;
    private String lastName;
    private List<Handicap> handicapList;
    private String firstName;
    private String profile;
    private Map<Badge, List<Activity>> badges;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public Map<Badge, List<Activity>> getBadges()
    {

        if (badges == null)
        {
            badges = new HashMap<>();
        }
        return badges;
    }

    public void setBadges(Map<Badge, List<Activity>> badges)
    {
        this.badges = badges;
    }

    public List<Handicap> getHandicapList()
    {
        if (this.handicapList == null)
        {
            this.handicapList = new ArrayList<>();
        }
        return handicapList;
    }

    public void setHandicapList(List<Handicap> handicapList)
    {
        this.handicapList = handicapList;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Athlete athlete = (Athlete) o;

        if (lastName != null ? !lastName.equals(athlete.lastName) : athlete.lastName != null)
        {
            return false;
        }
        return handicapList != null ? handicapList.equals(athlete.handicapList) : athlete.handicapList == null;
    }

    @Override
    public int hashCode()
    {
        int result = lastName != null ? lastName.hashCode() : 0;
        result = 31 * result + (handicapList != null ? handicapList.hashCode() : 0);
        return result;
    }

    public double getHandicapForDate(Date startDateLocal)
    {
        OptionalDouble handicap = handicapList.stream()
            .filter(h -> h.getTimestamp().before(startDateLocal))
            .sorted(Comparator.comparing(Handicap::getTimestamp).reversed())
            .mapToDouble(Handicap::getHandicap)
            .findFirst();


        if (!handicap.isPresent())
        {
            handicap = handicapList.stream()
                .sorted(Comparator.comparing(Handicap::getTimestamp))
                .mapToDouble(Handicap::getHandicap)
                .findFirst();
        }

        if (!handicap.isPresent())
        {
            return 1;
        }

        return handicap.getAsDouble();
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getProfile()
    {
        return profile;
    }

    public void setProfile(String profile)
    {
        this.profile = profile;
    }

    public void addBadge(Badge badge, Activity activity)
    {
        Map<Badge, List<Activity>> badges = getBadges();
        if (badges.containsKey(badge))
        {
            badges.get(badge).add(activity);
        } else
        {
            badges.put(badge, Arrays.asList(activity));
        }

    }
}
