package com.inventorsoft.english.lessons.service;

import com.inventorsoft.english.attendance.repository.AttendanceRepository;
import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.domain.dto.LessonDto;
import com.inventorsoft.english.lessons.domain.mapper.LessonMapper;
import com.inventorsoft.english.lessons.repository.LessonRepository;
import com.inventorsoft.english.uploadedfiles.service.UploadedFilesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final AttendanceRepository attendanceRepository;
    private final UploadedFilesService uploadedFilesService;
    private final LessonMapper lessonMapper;

    @Transactional(readOnly = true)
    public Page<LessonDto> getLessons(Long groupId, Pageable pageable) {
        return lessonRepository.findAllByEnglishGroupId(groupId, pageable)
                .map(lessonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<LessonDto> getLessons(LocalDate localDate, Pageable pageable) {
        Page<Lesson> lessons = isNull(localDate)
                ? lessonRepository.findAll(pageable)
                : lessonRepository.findAllByStartTimeBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay(), pageable);
        return lessons
                .map(lessonMapper::toDto);
    }

    @Transactional(readOnly = true)
    public LessonDto getLessonById(Long id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
    }

    @Transactional(readOnly = true)
    public List<Lesson> findLessonsByGroup(Long groupId) {
        return lessonRepository.findAllByEnglishGroupId(groupId, Pageable.unpaged())
                .getContent();
    }

    @Transactional(readOnly = true)
    public List<Lesson> findLessonsAfterDate(Long groupId, LocalDateTime date) {
        return lessonRepository.findAllByEnglishGroupIdAndStartTimeAfter(groupId, date);
    }

    @Transactional
    public void saveLessons(List<Lesson> lessons) {
        lessonRepository.saveAll(lessons);
    }

    @Transactional
    public LessonDto updateLesson(Long id, LessonDto lessonDto) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found!"));
        lessonMapper.update(lessonDto, lesson);
        lessonRepository.save(lesson);
        return lessonMapper.toDto(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        uploadedFilesService.unbindFilesFromLesson(id);
        attendanceRepository.deleteAll(attendanceRepository.findAllByLessonId(id));
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteAll(List<Lesson> lessons) {
        lessonRepository.deleteAllInBatch(lessons);
    }
}
