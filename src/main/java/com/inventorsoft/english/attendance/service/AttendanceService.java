package com.inventorsoft.english.attendance.service;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.attendance.domain.dto.AttendanceDto;
import com.inventorsoft.english.attendance.domain.mapper.AttendanceMapper;
import com.inventorsoft.english.attendance.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceMapper attendanceMapper;

    @Transactional
    public void saveAttendances(List<Attendance> attendances) {
        attendanceRepository.saveAll(attendances);
    }

    @Transactional
    public void updateAttendance(Long id, boolean isPresent) {
        Attendance attendance = attendanceRepository.findById(id).orElseThrow();
        attendance.setIsPresent(isPresent);
        attendanceRepository.save(attendance);
    }

    @Transactional(readOnly = true)
    public List<Attendance> getByLesson(Long lessonId) {
        return attendanceRepository.findAllByLessonId(lessonId);
    }

    @Transactional(readOnly = true)
    public List<AttendanceDto> findAllByLesson(Long lessonId) {
        return attendanceRepository.findAllByLessonId(lessonId)
                .stream()
                .map(attendanceMapper::toDto)
                .toList();
    }

    @Transactional
    public void delete(Attendance attendance) {
        try {
            attendanceRepository.delete(attendance);
        } catch (EmptyResultDataAccessException e) {
        }
    }

    @Transactional
    public void deleteAllByLessons(List<Long> lessonIds) {
        attendanceRepository.deleteAllByLessonIdIn(lessonIds);
    }
}
