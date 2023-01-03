package com.inventorsoft.english.scheduler.service;

import com.inventorsoft.english.general.config.DateGenerator;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.repository.LessonRepository;
import com.inventorsoft.english.scheduler.config.SchedulerProperties;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.domain.Occurrence;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AppointmentRuleResolverTest {

    @Mock
    private SchedulerProperties schedulerProperties;
    @Mock
    private DateGenerator dateGenerator;
    @Mock
    private LessonRepository lessonRepository;

    @InjectMocks
    private AppointmentRuleResolver resolver;

    @Test
    void scheduleLessonMonThu() {
        when(schedulerProperties.getIntervalDays())
                .thenReturn(7);
        when(dateGenerator.generateLocalDate())
                .thenReturn(LocalDate.of(2022, 12, 12));
        when(lessonRepository.findAllByEnglishGroupIdAndStartTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        AppointmentRule appointmentRule = buildAppointmentRule();
        appointmentRule.setMon(true);
        appointmentRule.setThur(true);

        List<Lesson> actualLessons = resolver.resolveRule(appointmentRule);
        assertThat(actualLessons)
                .isNotEmpty()
                .hasSize(2)
                .map(lesson -> Tuple.tuple(lesson.getStartTime(), lesson.getEndTime()))
                .containsExactly(
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 12, 14, 0),
                                LocalDateTime.of(2022, 12, 12, 15, 0)
                        ),
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 15, 14, 0),
                                LocalDateTime.of(2022, 12, 15, 15, 0)
                        )
                );

        InOrder order = inOrder(schedulerProperties, dateGenerator, lessonRepository);
        order.verify(dateGenerator).generateLocalDate();
        order.verify(schedulerProperties).getIntervalDays();
        order.verify(lessonRepository).findAllByEnglishGroupIdAndStartTimeBetween(1L,
                LocalDate.of(2022, 12, 12).atStartOfDay(),
                LocalDate.of(2022, 12, 19).atStartOfDay());
        order.verifyNoMoreInteractions();
    }

    @Test
    void scheduleLessonMonWedFri_monExists() {
        when(schedulerProperties.getIntervalDays())
                .thenReturn(7);
        when(dateGenerator.generateLocalDate())
                .thenReturn(LocalDate.of(2022, 12, 12));
        when(lessonRepository.findAllByEnglishGroupIdAndStartTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(buildMonLesson()));

        AppointmentRule appointmentRule = buildAppointmentRule();
        appointmentRule.setMon(true);
        appointmentRule.setWed(true);
        appointmentRule.setFri(true);

        List<Lesson> actualLessons = resolver.resolveRule(appointmentRule);
        assertThat(actualLessons)
                .isNotEmpty()
                .hasSize(2)
                .map(lesson -> Tuple.tuple(lesson.getStartTime(), lesson.getEndTime()))
                .containsExactly(
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 14, 14, 0),
                                LocalDateTime.of(2022, 12, 14, 15, 0)
                        ),
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 16, 14, 0),
                                LocalDateTime.of(2022, 12, 16, 15, 0)
                        )
                );

        InOrder order = inOrder(schedulerProperties, dateGenerator, lessonRepository);
        order.verify(dateGenerator).generateLocalDate();
        order.verify(schedulerProperties).getIntervalDays();
        order.verify(lessonRepository).findAllByEnglishGroupIdAndStartTimeBetween(1L,
                LocalDate.of(2022, 12, 12).atStartOfDay(),
                LocalDate.of(2022, 12, 19).atStartOfDay());
        order.verifyNoMoreInteractions();
    }

    @Test
    void scheduleLessonMonWedFri() {
        when(schedulerProperties.getIntervalDays())
                .thenReturn(7);
        when(dateGenerator.generateLocalDate())
                .thenReturn(LocalDate.of(2022, 12, 12));
        when(lessonRepository.findAllByEnglishGroupIdAndStartTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        AppointmentRule appointmentRule = buildAppointmentRule();
        appointmentRule.setMon(true);
        appointmentRule.setWed(true);
        appointmentRule.setFri(true);

        List<Lesson> actualLessons = resolver.resolveRule(appointmentRule);
        assertThat(actualLessons)
                .isNotEmpty()
                .hasSize(3)
                .map(lesson -> Tuple.tuple(lesson.getStartTime(), lesson.getEndTime()))
                .containsExactly(
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 12, 14, 0),
                                LocalDateTime.of(2022, 12, 12, 15, 0)
                        ),
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 14, 14, 0),
                                LocalDateTime.of(2022, 12, 14, 15, 0)
                        ),
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 16, 14, 0),
                                LocalDateTime.of(2022, 12, 16, 15, 0)
                        )
                );

        InOrder order = inOrder(schedulerProperties, dateGenerator, lessonRepository);
        order.verify(dateGenerator).generateLocalDate();
        order.verify(schedulerProperties).getIntervalDays();
        order.verify(lessonRepository).findAllByEnglishGroupIdAndStartTimeBetween(1L,
                LocalDate.of(2022, 12, 12).atStartOfDay(),
                LocalDate.of(2022, 12, 19).atStartOfDay());
        order.verifyNoMoreInteractions();
    }

    @Test
    void scheduleLessonWedFri() {
        when(schedulerProperties.getIntervalDays())
                .thenReturn(7);
        when(dateGenerator.generateLocalDate())
                .thenReturn(LocalDate.of(2022, 12, 12));
        when(lessonRepository.findAllByEnglishGroupIdAndStartTimeBetween(anyLong(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        AppointmentRule appointmentRule = buildAppointmentRule();
        appointmentRule.setWed(true);
        appointmentRule.setFri(true);

        List<Lesson> actualLessons = resolver.resolveRule(appointmentRule);
        assertThat(actualLessons)
                .isNotEmpty()
                .hasSize(2)
                .map(lesson -> Tuple.tuple(lesson.getStartTime(), lesson.getEndTime()))
                .containsExactly(
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 14, 14, 0),
                                LocalDateTime.of(2022, 12, 14, 15, 0)
                        ),
                        Tuple.tuple(
                                LocalDateTime.of(2022, 12, 16, 14, 0),
                                LocalDateTime.of(2022, 12, 16, 15, 0)
                        )
                );
    }

    private AppointmentRule buildAppointmentRule() {
        EnglishGroup englishGroup = new EnglishGroup();
        englishGroup.setId(1L);

        return new AppointmentRule()
                .setStartTime(LocalTime.of(14, 0))
                .setEndTime(LocalTime.of(15, 0))
                .setOccurrence(Occurrence.WEEKLY)
                .setEnglishGroup(englishGroup)
                .setDaysSelected(new HashSet<>());
    }

    private Lesson buildMonLesson() {
        EnglishGroup group = new EnglishGroup();
        group.setId(1L);

        Lesson lesson = new Lesson();
        lesson.setId(1L);
        return lesson.setEnglishGroup(group)
                .setStartTime(LocalDateTime.of(LocalDate.of(2022, 12, 12), LocalTime.of(14, 0)))
                .setEndTime(LocalDateTime.of(LocalDate.of(2022, 12, 12), LocalTime.of(15, 0)));
    }
}
