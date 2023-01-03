package com.inventorsoft.english.lessons.controller;

import com.inventorsoft.english.lessons.domain.dto.LessonDto;
import com.inventorsoft.english.lessons.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/lessons")
@RequiredArgsConstructor
public class LessonRestController {

    private final LessonService lessonService;

    @GetMapping
    public Page<LessonDto> getLessons(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                      @PageableDefault Pageable pageable) {
        return lessonService.getLessons(date, pageable);
    }

    @GetMapping("/group/{groupId}")
    public Page<LessonDto> getLessons(@PathVariable Long groupId,
                                      @PageableDefault Pageable pageable) {
        return lessonService.getLessons(groupId, pageable);
    }

    @GetMapping("/{id}")
    public LessonDto getLessonById(@PathVariable Long id) {
        return lessonService.getLessonById(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public LessonDto updateLesson(@PathVariable Long id,
                                  @RequestBody @Valid LessonDto lessonDto) {
        return lessonService.updateLesson(id, lessonDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
