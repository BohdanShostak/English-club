package com.inventorsoft.english.scheduler.domain.mapper;

import com.inventorsoft.english.scheduler.domain.AppointmentRule;
import com.inventorsoft.english.scheduler.domain.dto.AppointmentRuleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppointmentRuleMapper {

    @Mapping(ignore = true, target = "id")
    AppointmentRule toEntity(AppointmentRuleDto dto);

    @Mapping(source = "englishGroup.id", target = "groupId")
    AppointmentRuleDto toDto(AppointmentRule appointmentRule);

    @Mapping(target = "id", ignore = true)
    void update(AppointmentRuleDto dto, @MappingTarget AppointmentRule entity);
}
