package com.inventorsoft.english.attendance.domain.dto;

import com.inventorsoft.english.lessons.domain.dto.LessonDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceDto {

    private Long id;
    private LessonDto lesson;
    private Long userId;
    private Boolean isPresent;
}
