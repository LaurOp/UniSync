package com.example.unisync.Repository;

import com.example.unisync.Model.Course;
import com.example.unisync.Model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends BaseRepository<Course, Long> {

    @Query("SELECT c FROM Course c WHERE c.id = :courseId")
    Optional<Course> findById(@Param("courseId") Long courseId);

    List<Course> findByCreatedBy(AppUser createdBy);

    List<Course> findByAdmin(AppUser admin);

    @Query("SELECT c FROM Course c WHERE c.meetings IS NOT EMPTY")
    List<Course> findCoursesWithMeetings();

    @Query("SELECT c FROM Course c JOIN FETCH c.messages WHERE c.id = :courseId")
    Course findCourseWithMessages(@Param("courseId") Long courseId);

    @Query("SELECT c.students FROM Course c WHERE c.id = :courseId")
    List<AppUser> findUsersByCourseId(Long courseId);
}
