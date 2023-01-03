package com.inventorsoft.english.attendance.repository;

import com.inventorsoft.english.attendance.domain.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByLessonId(Long lessonId);

    @Modifying
    @Query("UPDATE Attendance a SET a.user = null ")
    void removeUsersFromAttendances();

    void deleteAllByLessonIdIn(List<Long> lessonId);
}
