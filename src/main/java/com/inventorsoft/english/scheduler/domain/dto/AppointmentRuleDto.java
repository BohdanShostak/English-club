package com.inventorsoft.english.scheduler.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventorsoft.english.scheduler.domain.Occurrence;
import java.time.Duration;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentRuleDto {

    private Long id;
    private Long groupId;

    @NotNull(message = "startTime is required")
    private LocalTime startTime;

    @NotNull(message = "endTime is required")
    private LocalTime endTime;

    @NotNull(message = "occurrence is required")
    private Occurrence occurrence;
    private Duration duration;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thur;
    private boolean fri;
    private boolean sat;
    private boolean sun;
}
