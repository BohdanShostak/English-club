package com.inventorsoft.english.groups.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.scheduler.domain.dto.AppointmentRuleDto;
import com.inventorsoft.english.users.domain.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupDto {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Valid
    private AppointmentRuleDto rule;

    private UserDto mentor;

    @NotNull
    private EnglishLevel englishLevel;

}
