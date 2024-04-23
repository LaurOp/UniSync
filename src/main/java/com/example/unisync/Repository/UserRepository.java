package com.example.unisync.Repository;

import com.example.unisync.Model.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<AppUser, Long> {
   @Query("SELECT u FROM AppUser u WHERE u.id = :userId")
   Optional<AppUser> findById(@Param("userId") Long userId);

   @Query("SELECT u FROM AppUser u WHERE u.username = :username")
    Optional<AppUser> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM AppUser u WHERE u.isUniversity = true")
    List<AppUser> findUniversities();

    @Query("SELECT u FROM AppUser u WHERE u.isTeacher = true")
    List<AppUser> findTeachers();

    @Query("SELECT u FROM AppUser u WHERE u.isUniversity = false AND u.isTeacher = false")
    List<AppUser> findUsers();

    @Query("SELECT u FROM AppUser u WHERE u.createdCourses IS NOT EMPTY")
    List<AppUser> findUsersWithCreatedCourses();

    @Query("SELECT u FROM AppUser u WHERE u.administeredCourses IS NOT EMPTY")
    List<AppUser> findUsersWithAdministeredCourses();

    @Query("SELECT u FROM AppUser u JOIN FETCH u.messages WHERE u.id = :userId")
    Optional<AppUser> findUserWithMessages(@Param("userId") Long userId);

    @Query("SELECT u FROM AppUser u WHERE u.id IN :userIds")
    List<AppUser> findUsersByIds(List<Long> userIds);

    @Query("SELECT u FROM AppUser u JOIN FETCH u.enrolledCourses c WHERE c.id = :courseId")
    List<AppUser> findAllUsersInACourse(@Param("courseId") Long courseId);
}
