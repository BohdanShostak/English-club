package com.inventorsoft.english.scheduler.service;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.attendance.service.AttendanceResolver;
import com.inventorsoft.english.attendance.service.AttendanceService;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.service.LessonService;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonScheduler {
    private final AppointmentRuleService ruleService;
    private final LessonService lessonService;
    private final AppointmentRuleResolver ruleResolver;
    private final AttendanceResolver attendanceResolver;
    private final AttendanceService attendanceService;


    @Transactional
    public void scheduleLessons() {
        List<AppointmentRule> appointmentRules = ruleService.getRules();
        List<Lesson> lessonsToSchedule = createLessons(appointmentRules);
        List<Attendance> createdAttendances = attendanceResolver.createAttendances(lessonsToSchedule);
        lessonService.saveLessons(lessonsToSchedule);
        attendanceService.saveAttendances(createdAttendances);
        ruleService.deleteNonRecurringRules();
    }

    public List<Lesson> createLessons(List<AppointmentRule> appointmentRules) {
        return appointmentRules
                .stream()
                .map(ruleResolver::resolveRule)
                .flatMap(List::stream)
                .toList();
    }
}
