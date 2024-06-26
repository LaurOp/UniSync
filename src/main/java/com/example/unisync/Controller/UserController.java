package com.example.unisync.Controller;

import com.example.unisync.Config.Auth.UserInfoDetails;
import com.example.unisync.Mapper.UserMapper;
import com.example.unisync.Model.Course;
import com.example.unisync.Model.UserInfo;
import com.example.unisync.Service.CourseService;
import com.example.unisync.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.unisync.Config.Constants.*;

@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController{
    private final UserService userService;
    private final CourseService courseService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, CourseService courseService, UserMapper userMapper) {
        this.userService = userService;
        this.courseService = courseService;
        this.userMapper = userMapper;
    }

    @PostMapping("/CreateCourse")
    @PreAuthorize("hasAuthority('UNIVERSITY')")
    public ResponseEntity<String> createCourseForUserEndpoint(@RequestBody Course course) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());

            try{
                currentUser.get();

            } catch (Exception ignored){
                return new ResponseEntity<>(USER_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            userService.createCourseIfUniversity(currentUser.get(), course);
            return new ResponseEntity<>(COURSE_CREATED_SUCCESSFULLY, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_CREATING_COURSE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @PostMapping("/create")
//    public ResponseEntity<UserDTO> createUserEndpoint(@Valid @RequestBody UserDTO user) {
//        try {
//            AppUser createdUser = userService.createUser(userMapper.map(user));
//            return new ResponseEntity<>(userMapper.map(createdUser), HttpStatus.CREATED);
//        } catch (ValidationException e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @PreAuthorize("hasAnyAuthority('UNIVERSITY','TEACHER')")
    @PutMapping("/{userId}/assign-course-admin/{courseId}")
    public ResponseEntity<String> assignCourseAdmin(
            @PathVariable Long userId,
            @PathVariable Long courseId) {

        try {
            Optional<UserInfo> userOptional = userService.getById(userId);
            Optional<Course> courseOptional = courseService.getById(courseId);

            if (userOptional.isEmpty() || courseOptional.isEmpty()) {
                return new ResponseEntity<>(USER_OR_COURSE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            UserInfo user = userOptional.get();
            Course course = courseOptional.get();

            if (user.getRoles().contains("TEACHER")){
                course.setAdmin(user);
                courseService.save(course);
                return new ResponseEntity<>(USER_ASSIGNED_AS_COURSE_ADMIN_SUCCESSFULLY, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(ADMIN_MUST_BE_A_TEACHER, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_ASSIGNING_COURSE_ADMIN + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAnyAuthority('UNIVERSITY','TEACHER')")
    @PostMapping("/{userId}/add-student-to-course/{courseId}")
    public ResponseEntity<String> addStudentToCourse(
            @PathVariable Long userId,
            @PathVariable Long courseId) {

        try {
            Optional<UserInfo> userOptional = userService.getById(userId);
            Optional<Course> courseOptional = courseService.getById(courseId);

            if (userOptional.isEmpty() || courseOptional.isEmpty()) {
                return new ResponseEntity<>(USER_OR_COURSE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            UserInfo user = userOptional.get();
            Course course = courseOptional.get();

            if (!user.getRoles().contains("UNIVERSITY")) {
                course.getStudents().add(user);
                courseService.save(course);
                return new ResponseEntity<>(TO_COURSE_SUCCESSFULLY, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(DOES_NOT_HAVE_PERMISSION, HttpStatus.FORBIDDEN);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_ADDING_STUDENT_TO_COURSE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/enroll-in-course/{courseId}")
    public ResponseEntity<String> enrollInCourse(
            @PathVariable Long courseId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
        var currentUser = userService.getByName(currentUserDetails.getUsername());

        try {
            Optional<Course> courseOptional = courseService.getById(courseId);

            if (currentUser.isEmpty() || courseOptional.isEmpty()) {
                return new ResponseEntity<>(USER_OR_COURSE_NOT_FOUND, HttpStatus.NOT_FOUND);
            }

            UserInfo student = currentUser.get();
            Course course = courseOptional.get();

            if (student.getEnrolledCourses().contains(course)) {
                return new ResponseEntity<>(ALREADY_ENROLLED_IN_THE_COURSE, HttpStatus.BAD_REQUEST);
            }

            userService.enrollStudentInCourse(student, course);

            return new ResponseEntity<>(ENROLLED_IN_THE_COURSE_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_ENROLLING_IN_THE_COURSE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
