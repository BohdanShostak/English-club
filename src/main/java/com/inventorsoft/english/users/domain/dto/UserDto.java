package com.inventorsoft.english.users.domain.dto;

import com.inventorsoft.english.groups.domain.dto.GroupDto;
import com.inventorsoft.english.general.domain.EnglishLevel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDto {

    private Long id;

    private GroupDto englishGroup;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private EnglishLevel englishLevel;

    private String userInfo;

    private Long avatarId;
}
