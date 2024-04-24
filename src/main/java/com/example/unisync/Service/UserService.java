package com.example.unisync.Service;

import com.example.unisync.Model.Course;
import com.example.unisync.Model.UserInfo;
import com.example.unisync.Repository.CourseRepository;
import com.example.unisync.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements BaseService<UserInfo> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserService(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public void createCourseIfUniversity(UserInfo user) {
        if (user.getRoles().contains("UNIVERSITY")) {
            Course newCourse = new Course();
            newCourse.setCreatedBy(user);
            newCourse.setAdmin(user);

            courseRepository.save(newCourse);
            userRepository.save(user);
        }
    }

    public void createCourseIfUniversity(UserInfo user, Course newCourse) {
        if (user.getRoles().contains("UNIVERSITY")) {
            newCourse.setCreatedBy(user);
            newCourse.setAdmin(user);

            courseRepository.save(newCourse);
            userRepository.save(user);
        }
    }

    public void enrollStudentInCourse(UserInfo student, Course course) {
        student.getEnrolledCourses().add(course);
        course.getStudents().add(student);
        courseRepository.save(course);
        userRepository.save(student);
    }

    public UserInfo createUser(UserInfo user) {
        return userRepository.save(user);
    }

    @Override
    public List<UserInfo> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserInfo> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
