package com.inventorsoft.english.attendance.controller;

import com.inventorsoft.english.attendance.domain.dto.AttendanceDto;
import com.inventorsoft.english.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceRestController {

    private final AttendanceService attendanceService;

    @PatchMapping("/{id}")
    public void updatePresent(@PathVariable Long id, @RequestBody boolean present) {
        attendanceService.updateAttendance(id, present);
    }

    @GetMapping("/lesson/{lessonId}")
    public List<AttendanceDto> getAllByLesson(@PathVariable Long lessonId) {
        return attendanceService.findAllByLesson(lessonId);
    }
}
