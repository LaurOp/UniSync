package com.example.unisync.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Invitation extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "invited_user_id")
    private AppUser invitedUser;

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public AppUser getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(AppUser invitedUser) {
        this.invitedUser = invitedUser;
    }
}
