package no.bouvet.sandvika.activityboard.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Club {
    private String id;
    private List<Integer> memberIds;
    private Date competitionStartDate;
    private Date competitionEndDate;
    private List<Integer> adminIds;

    public String getId() {
        return id;
    }

    public void setId(String name) {
        this.id = name;
    }

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public Date getCompetitionStartDate() {
        return competitionStartDate;
    }

    public void setCompetitionStartDate(Date competitionStartDate) {
        this.competitionStartDate = competitionStartDate;
    }

    public List<Integer> getAdminIds() {
        return adminIds;
    }

    public void setAdminIds(List<Integer> adminIds) {
        this.adminIds = adminIds;
    }

    public Date getCompetitionEndDate() {
        return competitionEndDate;
    }

    public void setCompetitionEndDate(Date competitionEndDate) {
        this.competitionEndDate = competitionEndDate;
    }

    public void addMember(Integer memberId) {
        if (memberIds == null) {
            memberIds = new ArrayList<>();
        }
        memberIds.add(memberId);
    }

    public void addAdmin(Integer adminId) {
        if (adminIds == null) {
            adminIds = new ArrayList<>();
        }
        adminIds.add(adminId);
    }
}
