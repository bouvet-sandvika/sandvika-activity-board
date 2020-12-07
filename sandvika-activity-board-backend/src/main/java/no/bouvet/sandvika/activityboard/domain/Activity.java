package no.bouvet.sandvika.activityboard.domain;

import no.bouvet.sandvika.activityboard.domain.weather.Weather;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

import java.util.*;

public class Activity {
    @Id
    private long id;
    private double points;
    private String name;
    private String athleteLastName;
    private String athleteFirstName;
    private String description;
    private double distanceInMeters;
    private int movingTimeInSeconds;
    private int elapsedTimeInSeconds;
    private double totalElevationGaininMeters;
    private String type;
    private Date startDateLocal;
    private String timezone;
    @GeoSpatialIndexed
    private double[] startLatLng;
    @GeoSpatialIndexed
    private double[] endLatLng;
    private int achievementCount;
    private int kudosCount;
    private int commentCount;
    private int athleteCount;
    private boolean trainer;
    private boolean commute;
    private boolean manual;
    private float averageSpeedInMetersPerSecond;
    private float maxSpeedInMetersPerSecond;
    private boolean hasHeartrate;
    private float averageHeartrate;
    private int maxHeartrate;
    private double kilojoules;
    private int sufferScore;
    private double handicap;
    private Integer athleteId;
    private Set<Badge> badges;
    private List<Photo> photos;
    private Weather weather;

    public Activity() {
    }

    public Activity(long id, int points, String athleteLastName, String athleteFirstName) {
        this.id = id;
        this.points = points;
        this.athleteFirstName = athleteFirstName;
        this.athleteLastName = athleteLastName;
    }

    public Activity(long id, int points, String athleteLastName, String athleteFirstName, Date startDateLocal) {
        this.id = id;
        this.points = points;
        this.athleteFirstName = athleteFirstName;
        this.athleteLastName = athleteLastName;
        this.startDateLocal = startDateLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Badge> getBadges() {
        return badges;
    }

    public void setBadges(Set<Badge> badges) {
        this.badges = badges;
    }

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAthleteLastName() {
        return athleteLastName;
    }

    public void setAthleteLastName(String athleteLastName) {
        this.athleteLastName = athleteLastName;
    }

    public String getAthleteFirstName() {
        return athleteFirstName;
    }

    public void setAthleteFirstName(String athleteFirstName) {
        this.athleteFirstName = athleteFirstName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDistanceInMeters() {
        return distanceInMeters;
    }

    public void setDistanceInMeters(double distanceInMeters) {
        this.distanceInMeters = distanceInMeters;
    }

    public int getMovingTimeInSeconds() {
        return movingTimeInSeconds;
    }

    public void setMovingTimeInSeconds(int movingTimeInSeconds) {
        this.movingTimeInSeconds = movingTimeInSeconds;
    }

    public int getElapsedTimeInSeconds() {
        return elapsedTimeInSeconds;
    }

    public void setElapsedTimeInSeconds(int elapsedTimeInSeconds) {
        this.elapsedTimeInSeconds = elapsedTimeInSeconds;
    }

    public double getTotalElevationGaininMeters() {
        return totalElevationGaininMeters;
    }

    public void setTotalElevationGaininMeters(double totalElevationGaininMeters) {
        this.totalElevationGaininMeters = totalElevationGaininMeters;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }

    public Date getStartDateLocal() {
        return startDateLocal;
    }

    public void setStartDateLocal(Date startDateLocal) {
        this.startDateLocal = startDateLocal;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public double[] getStartLatLng() {
        return startLatLng;
    }

    public void setStartLatLng(double[] startLatLng) {
        this.startLatLng = startLatLng;
    }

    public double[] getEndLatLng() {
        return endLatLng;
    }

    public void setEndLatLng(double[] endLatLng) {
        this.endLatLng = endLatLng;
    }

    public int getAchievementCount() {
        return achievementCount;
    }

    public void setAchievementCount(int achievementCount) {
        this.achievementCount = achievementCount;
    }

    public int getKudosCount() {
        return kudosCount;
    }

    public void setKudosCount(int kudosCount) {
        this.kudosCount = kudosCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getAthleteCount() {
        return athleteCount;
    }

    public void setAthleteCount(int athleteCount) {
        this.athleteCount = athleteCount;
    }

    public boolean isTrainer() {
        return trainer;
    }

    public void setTrainer(boolean trainer) {
        this.trainer = trainer;
    }

    public boolean isCommute() {
        return commute;
    }

    public void setCommute(boolean commute) {
        this.commute = commute;
    }

    public boolean isManual() {
        return manual;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public float getAverageSpeedInMetersPerSecond() {
        return averageSpeedInMetersPerSecond;
    }

    public void setAverageSpeedInMetersPerSecond(float averageSpeedInMetersPerSecond) {
        this.averageSpeedInMetersPerSecond = averageSpeedInMetersPerSecond;
    }

    public float getMaxSpeedInMetersPerSecond() {
        return maxSpeedInMetersPerSecond;
    }

    public void setMaxSpeedInMetersPerSecond(float maxSpeedInMetersPerSecond) {
        this.maxSpeedInMetersPerSecond = maxSpeedInMetersPerSecond;
    }

    public boolean isHasHeartrate() {
        return hasHeartrate;
    }

    public void setHasHeartrate(boolean hasHeartrate) {
        this.hasHeartrate = hasHeartrate;
    }

    public float getAverageHeartrate() {
        return averageHeartrate;
    }

    public void setAverageHeartrate(float averageHeartrate) {
        this.averageHeartrate = averageHeartrate;
    }

    public int getMaxHeartrate() {
        return maxHeartrate;
    }

    public void setMaxHeartrate(int maxHeartrate) {
        this.maxHeartrate = maxHeartrate;
    }

    public double getKiloJoules() {
        return kilojoules;
    }

    public void setKiloJoules(Double kiloJoules) {
        this.kilojoules = kiloJoules;
    }

    public int getSufferScore() {
        return sufferScore;
    }

    public void setSufferScore(int sufferScore) {
        this.sufferScore = sufferScore;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public void addPhoto(Photo photo) {
        if (photos == null) {
            photos = new ArrayList<>();
        }
        photos.add(photo);
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Activity activity = (Activity) o;

        return id == activity.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Activity{");
        sb.append("points=").append(points);
        sb.append(", name='").append(name).append('\'');
        sb.append(", athleteLastName='").append(athleteLastName).append('\'');
        sb.append(", athleteFirstName='").append(athleteFirstName).append('\'');
        sb.append(", distanceInMeters=").append(distanceInMeters);
        sb.append(", movingTimeInSeconds=").append(movingTimeInSeconds);
        sb.append(", type='").append(type).append('\'');
        sb.append(", achievementCount=").append(achievementCount);
        sb.append(", kudosCount=").append(kudosCount);
        sb.append('}');
        return sb.toString();
    }

    public double getHandicap() {
        return handicap;
    }

    public void setHandicap(double handicap) {
        this.handicap = handicap;
    }

    public Integer getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(Integer athleteId) {
        this.athleteId = athleteId;
    }

    public ActivitySummary getSummary() {
        ActivitySummary summary = new ActivitySummary();
        summary.setId(this.getId());
        summary.setName(this.getName());
        summary.setAthleteFirstName(this.getAthleteFirstName());
        summary.setAthleteLastName(this.getAthleteLastName());
        summary.setPoints(this.getPoints());
        summary.setDescription(this.getDescription());
        summary.setDistanceInMeters(this.getDistanceInMeters());
        summary.setMovingTimeInSeconds(this.getMovingTimeInSeconds());
        summary.setElapsedTimeInSeconds(this.getElapsedTimeInSeconds());
        summary.setTotalElevationGaininMeters(this.getTotalElevationGaininMeters());
        summary.setType(this.getType());
        summary.setStartDateLocal(this.getStartDateLocal());
        return summary;
    }
}
