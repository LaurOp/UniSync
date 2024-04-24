package com.example.unisync.DTO;

import com.example.unisync.Exception.Validation.EmailConstraint;
import com.example.unisync.Exception.Validation.PasswordConstraint;
import com.example.unisync.Exception.Validation.UsernameConstraint;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO extends BaseDto{

    private boolean isUniversity;
    private boolean isTeacher;

    @UsernameConstraint
    private String username;
    @PasswordConstraint
    private String password;
    @EmailConstraint
    private String email;

    private List<Long> enrolledCourseIds;

    public boolean isUniversity() {
        return isUniversity;
    }

    public void setUniversity(boolean university) {
        isUniversity = university;
    }

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }
}
