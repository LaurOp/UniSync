package com.example.unisync.Service;

import com.example.unisync.Model.Course;
import com.example.unisync.Model.AppUser;
import com.example.unisync.Repository.CourseRepository;
import com.example.unisync.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements BaseService<AppUser> {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public UserService(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public void createCourseIfUniversity(AppUser user) {
        if (user.isUniversity()) {
            Course newCourse = new Course();
            newCourse.setCreatedBy(user);
            newCourse.setAdmin(user);

            courseRepository.save(newCourse);
            userRepository.save(user);
        }
    }

    public void createCourseIfUniversity(AppUser user, Course newCourse) {
        if (user.isUniversity()) {
            newCourse.setCreatedBy(user);
            newCourse.setAdmin(user);

            courseRepository.save(newCourse);
            userRepository.save(user);
        }
    }

    public void enrollStudentInCourse(AppUser student, Course course) {
        student.getEnrolledCourses().add(course);
        course.getStudents().add(student);
        courseRepository.save(course);
        userRepository.save(student);
    }

    public AppUser createUser(AppUser user) {
        return userRepository.save(user);
    }

    @Override
    public List<AppUser> getAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<AppUser> getById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
