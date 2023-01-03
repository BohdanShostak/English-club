package com.inventorsoft.english.classrequest.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inventorsoft.english.userrequest.dto.UserRequestDto;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassRequestDto extends UserRequestDto {
}
