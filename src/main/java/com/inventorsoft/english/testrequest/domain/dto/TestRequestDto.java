package com.inventorsoft.english.testrequest.domain.dto;

import com.inventorsoft.english.general.domain.EnglishLevel;
import com.inventorsoft.english.userrequest.dto.UserRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestRequestDto extends UserRequestDto {
    private EnglishLevel result;
}
