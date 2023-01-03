package com.inventorsoft.english.classrequest.domain.mapper;

import com.inventorsoft.english.classrequest.domain.dto.ClassRequestDto;
import com.inventorsoft.english.classrequest.domain.model.ClassRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ClassRequestMapper {

    ClassRequestMapper INSTANCE = Mappers.getMapper(ClassRequestMapper.class);

    ClassRequestDto mapModelToDto(ClassRequest classRequest);

    ClassRequest mapDtoToModel(ClassRequestDto classRequestDto);

    List<ClassRequestDto> mapListOfClassRequestToListOfDto(List<ClassRequest> classRequest);

    @Mapping(target = "id", ignore = true)
    void updateClassRequestFromDto(@MappingTarget ClassRequest classRequest, ClassRequestDto classRequestDto);

}
