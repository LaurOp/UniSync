package com.example.unisync.Controller;

import com.example.unisync.Mapper.UserMapper;
import com.example.unisync.Service.CourseService;
import com.example.unisync.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.unisync.Config.Constants.USER_DELETED_SUCCES;

@Profile("dev")
@RestController
@RequestMapping("/api/users")
public class RestrictedUserController extends BaseController{

    private final UserService userService;

    @Autowired
    public RestrictedUserController(UserService userService, CourseService courseService, UserMapper userMapper) {
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('UNIVERSITY')")
    @DeleteMapping("/{id}")
    @Profile("dev")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(USER_DELETED_SUCCES);
    }
}
