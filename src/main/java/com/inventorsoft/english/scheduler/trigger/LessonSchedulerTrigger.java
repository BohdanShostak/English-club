package com.inventorsoft.english.scheduler.trigger;

import com.inventorsoft.english.scheduler.service.LessonScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class LessonSchedulerTrigger {

    private final LessonScheduler lessonScheduler;

    @Scheduled(cron = "${schedule.lesson-generator}")
    public void scheduleLessons() {
        log.info("starting job schedule");
        lessonScheduler.scheduleLessons();

    }
}
