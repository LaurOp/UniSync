package com.example.unisync.Controller;

import com.example.unisync.Config.Auth.UserInfoDetails;
import com.example.unisync.DTO.MeetingAttendanceDTO;
import com.example.unisync.Mapper.MeetingAttendanceMapper;
import com.example.unisync.Model.MeetingAttendance;
import com.example.unisync.Service.MeetingAttendanceService;
import com.example.unisync.Service.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.unisync.Config.Constants.ATTENDANCE_MARKED_SUCCESSFULLY;
import static com.example.unisync.Config.Constants.ERROR_MARKING_ATTENDANCE;

@RestController
@RequestMapping("/api/attendance")
public class MeetingAttendanceController extends BaseController{
    private final MeetingAttendanceService meetingAttendanceService;
    private final MeetingAttendanceMapper meetingAttendanceMapper;
    private final UserService userService;

    @Autowired
    public MeetingAttendanceController(MeetingAttendanceService meetingAttendanceService, MeetingAttendanceMapper meetingAttendanceMapper, UserService userService) {
        this.meetingAttendanceService = meetingAttendanceService;
        this.meetingAttendanceMapper = meetingAttendanceMapper;
        this.userService = userService;
    }

    @PostMapping("/mark-as")
    public ResponseEntity<String> markAttendance(@RequestBody MeetingAttendanceDTO attendanceDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());

            meetingAttendanceService.markAttendance(
                    currentUser.get().getId(),
                    attendanceDTO.getMeetingId(),
                    attendanceDTO.getAttendanceStatus()
            );
            return new ResponseEntity<>(ATTENDANCE_MARKED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_MARKING_ATTENDANCE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/future")
    public ResponseEntity<List<MeetingAttendanceDTO>> getFutureAttendancesForUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
            var currentUser = userService.getByName(currentUserDetails.getUsername());

            List<MeetingAttendance> futureAttendances = meetingAttendanceService.getFutureAttendancesForUser(currentUser.get().getId());
            return new ResponseEntity<>(meetingAttendanceMapper.map(futureAttendances), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
