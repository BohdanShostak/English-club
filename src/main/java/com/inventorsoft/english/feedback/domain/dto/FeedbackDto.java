package com.inventorsoft.english.feedback.domain.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FeedbackDto {

    @NotBlank
    private String anonymousFeedback;

    private LocalDateTime createdAt;
}
