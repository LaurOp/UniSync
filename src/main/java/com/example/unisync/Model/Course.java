package com.example.unisync.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Course extends BaseModel {

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private UserInfo createdBy;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private UserInfo admin;

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


    private List<UserInfo> students = new ArrayList<>();

    public Course(Long id) {
        super(id);
    }

    public Course() {
        super();
    }

    public UserInfo getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserInfo createdBy) {
        this.createdBy = createdBy;
    }

    public UserInfo getAdmin() {
        return admin;
    }

    public void setAdmin(UserInfo admin) {
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

    public List<UserInfo> getStudents() {
        return students;
    }

    public void setStudents(List<UserInfo> students) {
        this.students = students;
    }
}
