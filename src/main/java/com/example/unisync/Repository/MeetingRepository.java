package com.example.unisync.Repository;

import com.example.unisync.Model.Meeting;
import com.example.unisync.Model.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends BaseRepository<Meeting, Long>{

    @Query("SELECT m FROM Meeting m WHERE m.course.id = :courseId")
    List<Meeting> findByCourseId(Long courseId);

    @Query("SELECT m FROM Meeting m WHERE m.course.id = :courseId")
    Page<Meeting> findByCourseId(Long courseId, Pageable pageable);

    @Query("SELECT m FROM Meeting m WHERE m.createdBy = :createdBy AND m.startTime > :startTime")
    List<Meeting> findByCreatedByAndStartTimeAfter(UserInfo createdBy, LocalDateTime startTime);

}
