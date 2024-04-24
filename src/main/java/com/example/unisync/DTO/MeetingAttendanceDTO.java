package com.example.unisync.DTO;

import com.example.unisync.Config.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MeetingAttendanceDTO extends BaseDto{
        private Long meetingId;
        private AttendanceStatus attendanceStatus;


}
