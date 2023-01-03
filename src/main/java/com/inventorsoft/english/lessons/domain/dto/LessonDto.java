package com.inventorsoft.english.lessons.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LessonDto {

    private Long id;
    private Long groupId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
