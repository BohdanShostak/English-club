package com.inventorsoft.english.testrequest.domain.mapper;


import com.inventorsoft.english.testrequest.domain.dto.TestRequestDto;
import com.inventorsoft.english.testrequest.domain.model.TestRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TestRequestMapper {

    TestRequestMapper INSTANCE = Mappers.getMapper(TestRequestMapper.class);

    //@Mapping(source = "testRequest.user.id", target = "userId")
    TestRequestDto mapModelToDto(TestRequest testRequest);

    TestRequest mapDtoToModel(TestRequestDto testRequestDto);

    @Mapping(target = "id", ignore = true)
    void updateTestRequestFromDto(@MappingTarget TestRequest testRequest, TestRequestDto testRequestDto);

    List<TestRequestDto> mapListOfTestRequestToListOfDto(List<TestRequest> testRequest);

}
