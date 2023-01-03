package com.inventorsoft.english.feedback.domain.mapper;

import com.inventorsoft.english.feedback.domain.model.Feedback;
import com.inventorsoft.english.feedback.domain.dto.FeedbackDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    @Mapping(ignore = true, target = "id")
    Feedback toEntity(FeedbackDto dto);


    FeedbackDto toDto(Feedback feedback);

    @Mapping(target = "id", ignore = true)
    void update(FeedbackDto dto, @MappingTarget Feedback entity);
}
