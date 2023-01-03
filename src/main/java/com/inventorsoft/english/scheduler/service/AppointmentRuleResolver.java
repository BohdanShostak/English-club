package com.inventorsoft.english.scheduler.service;

import com.inventorsoft.english.general.config.DateGenerator;
import com.inventorsoft.english.general.domain.AbstractIdentifiable;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.repository.LessonRepository;
import com.inventorsoft.english.scheduler.config.SchedulerProperties;
import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.domain.Occurrence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class AppointmentRuleResolver {

    private final SchedulerProperties schedulerProperties;

    private final LessonRepository lessonRepository;
    private final DateGenerator dateGenerator;

    public List<Lesson> resolveRule(AppointmentRule rule) {
        Long groupId = Optional.of(rule)
                .map(AppointmentRule::getEnglishGroup)
                .map(AbstractIdentifiable::getId)
                .orElseThrow();

        LocalDate startDate = dateGenerator.generateLocalDate();
        LocalDate endDate = startDate.plusDays(schedulerProperties.getIntervalDays());

        Set<LocalDate> createdLessons = lessonRepository.findAllByEnglishGroupIdAndStartTimeBetween(
                groupId, startDate.atStartOfDay(), endDate.atStartOfDay())
                .stream()
                .map(Lesson::getStartTime)
                .map(LocalDateTime::toLocalDate)
                .collect(Collectors.toSet());

        Set<DayOfWeek> daysSelected = rule.getDaysSelected();

        Stream<Lesson> lessonsStream = startDate.datesUntil(endDate).toList()
                .stream()
                .filter(date -> daysSelected.contains(date.getDayOfWeek()))
                .map(date -> createLesson(date, rule.getStartTime(), rule.getEndTime(), rule.getEnglishGroup()))
                .filter(lesson -> !createdLessons.contains(lesson.getStartTime().toLocalDate()));

        if (Occurrence.ONCE.equals(rule.getOccurrence())) {
            lessonsStream = lessonsStream.limit(1);
        }
        return lessonsStream.toList();
    }

    private Lesson createLesson(LocalDate date, LocalTime startTime, LocalTime endTime, EnglishGroup englishGroup) {
        return new Lesson()
                .setStartTime(LocalDateTime.of(date, startTime))
                .setEndTime(LocalDateTime.of(date, endTime))
                .setEnglishGroup(englishGroup);
    }
}
