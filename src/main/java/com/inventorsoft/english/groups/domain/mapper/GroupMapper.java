package com.inventorsoft.english.groups.domain.mapper;

import com.inventorsoft.english.groups.domain.dto.GroupDto;
import com.inventorsoft.english.groups.domain.model.EnglishGroup;
import com.inventorsoft.english.scheduler.domain.mapper.AppointmentRuleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = AppointmentRuleMapper.class)
public interface GroupMapper {

    GroupMapper INSTANCE = Mappers.getMapper(GroupMapper.class);

    GroupDto mapModelToDto(EnglishGroup group);

    EnglishGroup mapDtoToModel(GroupDto groupDto);

    List<GroupDto> mapListOfGroupToListOfDto(List<EnglishGroup> groups);

    @Mapping(target = "id", ignore = true)
    void updateGroupFromDto(@MappingTarget EnglishGroup group, GroupDto groupDto);

}
