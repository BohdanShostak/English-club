package com.inventorsoft.english.users.domain.mapper;

import com.inventorsoft.english.users.domain.dto.UserDto;
import com.inventorsoft.english.users.domain.dto.UserRegistrationDto;
import com.inventorsoft.english.users.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(ignore = true, target = "id")
    User toEntity(UserDto dto);

    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    void update(UserDto dto, @MappingTarget User entity);

    void update(UserRegistrationDto dto, @MappingTarget User entity);
}
