package com.inventorsoft.english.attendance.domain.mapper;

import com.inventorsoft.english.attendance.domain.Attendance;
import com.inventorsoft.english.attendance.domain.dto.AttendanceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "lesson.groupId", source = "lesson.englishGroup.id")
    AttendanceDto toDto(Attendance attendance);
}
