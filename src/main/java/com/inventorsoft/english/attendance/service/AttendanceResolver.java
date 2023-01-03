package com.inventorsoft.english.attendance.service;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceResolver {

    private final AttendanceService attendanceService;
    private final UserService userService;

    public List<Attendance> createAttendances(List<Lesson> lessons) {
        List<Attendance> allAttendances = new ArrayList<>();
        for (Lesson lesson : lessons) {
            Set<Long> usersWithCreatedAttendances = attendanceService.getByLesson(lesson.getId())
                    .stream()
                    .map(Attendance::getUser)
                    .map(User::getId)
                    .collect(Collectors.toSet());

            List<Attendance> attendancesForOneLesson = userService.findUsersByEnglishGroup(lesson.getEnglishGroup())
                    .stream()
                    .filter(user -> !usersWithCreatedAttendances.contains(user.getId()))
                    .map(user -> new Attendance(lesson, user))
                    .toList();
            allAttendances.addAll(attendancesForOneLesson);
        }
        return allAttendances;
    }
}
