package com.inventorsoft.english.userrequest.dto;

import com.inventorsoft.english.userrequest.model.RequestStatus;
import com.inventorsoft.english.users.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserRequestDto {

    protected RequestStatus requestStatus;

    protected UserDto userDto;

    protected LocalDateTime meetingDate;

    protected String notes;

    protected String meetingUrl;
}
