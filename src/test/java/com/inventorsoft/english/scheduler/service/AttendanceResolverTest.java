package com.inventorsoft.english.scheduler.service;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.attendance.service.AttendanceResolver;
import com.inventorsoft.english.attendance.service.AttendanceService;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.users.domain.model.User;
import com.inventorsoft.english.users.service.UserService;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AttendanceResolverTest {

    @Mock
    private AttendanceService attendanceService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AttendanceResolver resolver;

    @Test
    void createAttendances_zeroExisting() {
        List<Lesson> lessons = lessonsProvider();

        when(attendanceService.getByLesson(anyLong()))
                .thenReturn(List.of());
        when(userService.findUsersByEnglishGroup(any(EnglishGroup.class)))
                .thenReturn(usersProvider());

        assertThat(resolver.createAttendances(lessons))
                .hasSize(6)
                .map(attendance -> Tuple.tuple(attendance.getUser().getId(), attendance.getLesson().getId()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, 1L),
                        Tuple.tuple(2L, 1L),
                        Tuple.tuple(3L, 1L),
                        Tuple.tuple(1L, 2L),
                        Tuple.tuple(2L, 2L),
                        Tuple.tuple(3L, 2L)
                );

        InOrder order = inOrder(attendanceService, userService);
        order.verify(attendanceService).getByLesson(1L);
        order.verify(userService).findUsersByEnglishGroup(argThat(englishGroup -> englishGroup.getId().equals(1L)));
        order.verify(attendanceService).getByLesson(2L);
        order.verify(userService).findUsersByEnglishGroup(argThat(englishGroup -> englishGroup.getId().equals(1L)));
        order.verifyNoMoreInteractions();
    }

    @Test
    void createAttendances_hasExisting() {
        List<Lesson> lessons = lessonsProvider();

        when(attendanceService.getByLesson(anyLong()))
                .thenReturn(
                        List.of(new Attendance(lessons.get(0), usersProvider().get(1)))
                )
                .thenReturn(
                        List.of(new Attendance(lessons.get(1), usersProvider().get(2)))
                );
        when(userService.findUsersByEnglishGroup(any(EnglishGroup.class)))
                .thenReturn(usersProvider());

        assertThat(resolver.createAttendances(lessons))
                .hasSize(4)
                .map(attendance -> Tuple.tuple(attendance.getUser().getId(), attendance.getLesson().getId()))
                .containsExactlyInAnyOrder(
                        Tuple.tuple(1L, 1L),
                        Tuple.tuple(3L, 1L),
                        Tuple.tuple(1L, 2L),
                        Tuple.tuple(2L, 2L)
                );

        InOrder order = inOrder(attendanceService, userService);
        order.verify(attendanceService).getByLesson(1L);
        order.verify(userService).findUsersByEnglishGroup(argThat(englishGroup -> englishGroup.getId().equals(1L)));
        order.verify(attendanceService).getByLesson(2L);
        order.verify(userService).findUsersByEnglishGroup(argThat(englishGroup -> englishGroup.getId().equals(1L)));
        order.verifyNoMoreInteractions();
    }

    private List<Lesson> lessonsProvider() {
        EnglishGroup group = new EnglishGroup();
        group.setId(1L);

        Lesson lesson1 = new Lesson();
        lesson1.setId(1L);
        lesson1.setEnglishGroup(group);
        lesson1.setStartTime(LocalDateTime.of(2022, 12, 12, 13, 0));
        lesson1.setEndTime(LocalDateTime.of(2022, 12, 12, 14, 0));

        Lesson lesson2 = new Lesson();
        lesson2.setId(2L);
        lesson2.setEnglishGroup(group);
        lesson2.setStartTime(LocalDateTime.of(2022, 12, 15, 13, 0));
        lesson2.setEndTime(LocalDateTime.of(2022, 12, 15, 14, 0));

        return List.of(lesson1, lesson2);
    }

    private List<User> usersProvider() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user1@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("user3@example.com");

        return List.of(user1, user2, user3);
    }
}
