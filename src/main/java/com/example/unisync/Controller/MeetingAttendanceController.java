package com.example.unisync.Controller;

import com.example.unisync.DTO.MeetingAttendanceDTO;
import com.example.unisync.Mapper.MeetingAttendanceMapper;
import com.example.unisync.Model.MeetingAttendance;
import com.example.unisync.Service.MeetingAttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.unisync.Config.Constants.ATTENDANCE_MARKED_SUCCESSFULLY;
import static com.example.unisync.Config.Constants.ERROR_MARKING_ATTENDANCE;

@RestController
@RequestMapping("/api/attendance")
public class MeetingAttendanceController extends BaseController{
    private final MeetingAttendanceService meetingAttendanceService;
    private final MeetingAttendanceMapper meetingAttendanceMapper;

    @Autowired
    public MeetingAttendanceController(MeetingAttendanceService meetingAttendanceService, MeetingAttendanceMapper meetingAttendanceMapper) {
        this.meetingAttendanceService = meetingAttendanceService;
        this.meetingAttendanceMapper = meetingAttendanceMapper;
    }

    @PostMapping("/mark-as")
    public ResponseEntity<String> markAttendance(@RequestBody MeetingAttendanceDTO attendanceDTO) {
        try {
            meetingAttendanceService.markAttendance(
                    attendanceDTO.getUserId(),
                    attendanceDTO.getMeetingId(),
                    attendanceDTO.getAttendanceStatus()
            );
            return new ResponseEntity<>(ATTENDANCE_MARKED_SUCCESSFULLY, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ERROR_MARKING_ATTENDANCE + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/future/{userId}")
    public ResponseEntity<List<MeetingAttendanceDTO>> getFutureAttendancesForUser(@PathVariable Long userId) {
        try {
            List<MeetingAttendance> futureAttendances = meetingAttendanceService.getFutureAttendancesForUser(userId);
            return new ResponseEntity<>(meetingAttendanceMapper.map(futureAttendances), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
