package no.bouvet.sandvika.activityboard.domain;


public class HcClimbBoardEntry {
    private int athleteId;
    private String athleteLastName;
    private String athleteFirstName;
    private double climbNumber;

    public int getAthleteId() {
        return athleteId;
    }

    public void setAthleteId(int athleteId) {
        this.athleteId = athleteId;
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

    public double getClimbNumber() {
        return climbNumber;
    }

    public void setClimbNumber(double climbNumber) {
        this.climbNumber = climbNumber;
    }
}
