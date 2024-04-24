package com.example.unisync.Repository;

import com.example.unisync.Model.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<UserInfo, Long> {
   @Query("SELECT u FROM UserInfo u WHERE u.id = :userId")
   Optional<UserInfo> findById(@Param("userId") Long userId);

   @Query("SELECT u FROM UserInfo u WHERE u.name = :username")
    Optional<UserInfo> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserInfo u WHERE u.roles LIKE '%UNIVERSITY%'")
    List<UserInfo> findUniversities();

    @Query("SELECT u FROM UserInfo u WHERE u.roles LIKE '%TEACHER%'")
    List<UserInfo> findTeachers();

    @Query("SELECT u FROM UserInfo u WHERE u.roles LIKE '%USER%'")
    List<UserInfo> findUsers();

    @Query("SELECT u FROM UserInfo u WHERE u.createdCourses IS NOT EMPTY")
    List<UserInfo> findUsersWithCreatedCourses();

    @Query("SELECT u FROM UserInfo u WHERE u.administeredCourses IS NOT EMPTY")
    List<UserInfo> findUsersWithAdministeredCourses();

    @Query("SELECT u FROM UserInfo u JOIN FETCH u.messages WHERE u.id = :userId")
    Optional<UserInfo> findUserWithMessages(@Param("userId") Long userId);

    @Query("SELECT u FROM UserInfo u WHERE u.id IN :userIds")
    List<UserInfo> findUsersByIds(List<Long> userIds);

    @Query("SELECT u FROM UserInfo u JOIN FETCH u.enrolledCourses c WHERE c.id = :courseId")
    List<UserInfo> findAllUsersInACourse(@Param("courseId") Long courseId);
}
