package com.example.unisync.Controller;

import com.example.unisync.Config.Auth.UserInfoDetails;
import com.example.unisync.DTO.MeetingDTO;
import com.example.unisync.Exception.NotFoundException;
import com.example.unisync.Exception.UnauthorizedException;
import com.example.unisync.Mapper.MeetingMapper;
import com.example.unisync.Model.Meeting;
import com.example.unisync.Service.MeetingService;
import com.example.unisync.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController extends BaseController{

    private final MeetingService meetingService;
    private final MeetingMapper meetingMapper;
    private final UserService userService;

    @Autowired
    public MeetingController(MeetingService meetingService, UserService userService) {
        this.meetingService = meetingService;
        this.meetingMapper = new MeetingMapper();
        this.userService = userService;
    }

    @PreAuthorize("hasAuthority('TEACHER')")
    @PostMapping("/create/{courseId}")
    public ResponseEntity<MeetingDTO> createMeeting(
            @PathVariable Long courseId,
            @RequestBody MeetingDTO meetingDTO) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfoDetails currentUserDetails = (UserInfoDetails) authentication.getPrincipal();
        var currentUser = userService.getByName(currentUserDetails.getUsername());

        try {
            MeetingDTO createdMeeting = meetingMapper.map(meetingService.createMeetingWithInvitations(
                    currentUser.get().getId(),
                    courseId,
                    meetingMapper.map(meetingDTO),
                    meetingDTO.getInvitedUserIds()));
            return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
        } catch (NotFoundException | UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byCourse/{courseId}")
    public ResponseEntity<Page<MeetingDTO>> getMeetingsByCourseId(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestHeader(defaultValue = "startTime") String sort,
            @RequestParam(defaultValue = "asc") String direction) {
        try {
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            Pageable pageable = PageRequest.of(page, size, sortDirection, sort);
            Page<Meeting> meetings = meetingService.getMeetingsByCourseId(courseId, pageable);

            List<MeetingDTO> meetingDTOs = meetingMapper.map(meetings.getContent());
            Page<MeetingDTO> meetingPage = new PageImpl<>(meetingDTOs, pageable, meetings.getTotalElements());

            return new ResponseEntity<>(meetingPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
