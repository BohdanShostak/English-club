package com.inventorsoft.english.uploadedfiles.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventorsoft.english.googlestorage.model.FileType;
import com.inventorsoft.english.lessons.domain.dto.LessonDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UploadedFileDto {

    private Long id;

    private String fileName;

    private LocalDateTime createdAt;

    private Long createdBy;

    private LessonDto lesson;

    private FileType fileType;

}
