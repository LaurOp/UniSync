package com.example.unisync.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private AppUser createdBy;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AppUser admin;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Meeting> meetings = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )


    private List<AppUser> students = new ArrayList<>();

    public Course(Long id) {
        super(id);
    }

    public Course() {
        super();
    }

    public AppUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AppUser createdBy) {
        this.createdBy = createdBy;
    }

    public AppUser getAdmin() {
        return admin;
    }

    public void setAdmin(AppUser admin) {
        this.admin = admin;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<AppUser> getStudents() {
        return students;
    }

    public void setStudents(List<AppUser> students) {
        this.students = students;
    }
}
