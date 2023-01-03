package com.inventorsoft.english.scheduler.controller;

import com.inventorsoft.english.scheduler.service.LessonScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class SchedulerRestController {

    private final LessonScheduler lessonScheduler;

    @PostMapping
    public void schedulerLesson() {
        lessonScheduler.scheduleLessons();
    }
}
