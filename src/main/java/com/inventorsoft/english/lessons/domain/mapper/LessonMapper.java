package com.inventorsoft.english.lessons.domain.mapper;

import com.inventorsoft.english.lessons.domain.Lesson;
import com.inventorsoft.english.lessons.domain.dto.LessonDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "englishGroup.id", target = "groupId")
    LessonDto toDto(Lesson lesson);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "englishGroup", ignore = true)
    void update(LessonDto dto, @MappingTarget Lesson lesson);
}
