package com.example.unisync.Controller;

import com.example.unisync.DTO.AuthRequest;
import com.example.unisync.Config.Auth.JwtService;
import com.example.unisync.Service.UserInfoService;
import com.example.unisync.Model.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {
    private final UserInfoService service;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(UserInfoService userInfoService, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.service = userInfoService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("/welcome")
    public String welcomePage() {
        log.info("In welcome endpoint");
        return "Welcome; This is unprotected";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        log.info("Add user endpoint with username {}", userInfo.getName());
        return service.addUser(userInfo);
    }

    @GetMapping("/user/profile")
    @PreAuthorize("hasAuthority('USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/user/teacher")
    @PreAuthorize("hasAuthority('TEACHER')")
    public String teacherProfile() {
        return "Welcome to Teacher Profile";
    }

    @GetMapping("/user/university")
    @PreAuthorize("hasAuthority('UNIVERSITY')")
    public String universityProfile() {
        return "Welcome to University Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("Generating token for {}", authRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            log.debug("Is authenticated");
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            log.warn("Unauthenticated");
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

}

