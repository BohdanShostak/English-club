package com.inventorsoft.english.groups.service;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.attendance.service.AttendanceResolver;
import com.inventorsoft.english.attendance.service.AttendanceService;
import com.inventorsoft.english.general.config.DateGenerator;
import com.inventorsoft.english.groups.domain.dto.GroupDto;
import com.inventorsoft.english.groups.domain.mapper.GroupMapper;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.groups.repository.GroupsRepository;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.service.LessonService;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.service.AppointmentRuleResolver;
import com.inventorsoft.english.scheduler.service.AppointmentRuleService;
import com.inventorsoft.english.uploadedfiles.service.UploadedFilesService;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.repository.UserRepository;
import com.inventorsoft.english.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupManagementFacade {

    private final GroupService groupService;
    private final AppointmentRuleService appointmentRuleService;
    private final LessonService lessonService;
    private final AttendanceService attendanceService;
    private final UserService userService;
    private final UploadedFilesService uploadedFilesService;
    private final AppointmentRuleResolver appointmentRuleResolver;
    private final AttendanceResolver attendanceResolver;
    private final DateGenerator dateGenerator;
    private final GroupsRepository groupsRepository;
    private final UserRepository userRepository;

    private final GroupMapper groupMapper = GroupMapper.INSTANCE;

    @Transactional
    public GroupDto createGroup(GroupDto groupDto) {
        AppointmentRule appointmentRule = appointmentRuleService.save(groupDto.getRule());

        EnglishGroup group = groupService.create(groupDto);
        group.setRule(appointmentRule);
        groupsRepository.save(group);

        appointmentRule.setEnglishGroup(group);
        appointmentRuleService.save(appointmentRule);

        List<Lesson> lessonsToCreate = appointmentRuleResolver.resolveRule(appointmentRule);
        lessonService.saveLessons(lessonsToCreate);

        return groupMapper.mapModelToDto(group);
    }

    @Transactional
    public void addUserToGroup(Long userId, Long groupId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found!"));
        EnglishGroup group = groupsRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Group with id = " + groupId + " not found!"));
        user.setEnglishGroup(group);
        userRepository.save(user);
        log.info("User with id {} was added to Group with id {}", userId, groupId);

        List<Lesson> lessonsToCreateAttendancesFor = lessonService.findLessonsAfterDate(group.getId(),
                dateGenerator.generateLocalDateTime());
        List<Attendance> attendances = attendanceResolver.createAttendances(lessonsToCreateAttendancesFor);
        attendanceService.saveAttendances(attendances);
    }

    public void deleteGroup(Long id) {
        Optional<EnglishGroup> groupOptional = groupService.findById(id);
        if (groupOptional.isEmpty()) {
            return;
        }

        EnglishGroup group = groupOptional.get();
        List<Lesson> lessons = lessonService.findLessonsByGroup(group.getId());
        List<User> users = userService.findUsersByEnglishGroup(group);
        for (var user : users) {
            user.setEnglishGroup(null);
            userService.save(user);
        }

        for (var lesson : lessons) {
            attendanceService.getByLesson(lesson.getId())
                    .forEach(attendanceService::delete);
            uploadedFilesService.unbindFilesFromLesson(lesson.getId());
            lessonService.deleteLesson(lesson.getId());
        }
        groupService.deleteGroup(group.getId());
    }
}
