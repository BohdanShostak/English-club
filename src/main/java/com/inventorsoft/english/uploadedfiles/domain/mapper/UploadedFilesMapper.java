package com.inventorsoft.english.uploadedfiles.domain.mapper;

import com.inventorsoft.english.uploadedfiles.domain.dto.UploadedFileDto;
import com.inventorsoft.english.uploadedfiles.domain.model.UploadedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UploadedFilesMapper {

    UploadedFilesMapper INSTANCE = Mappers.getMapper(UploadedFilesMapper.class);

    @Mapping(target = "createdBy", source = "uploadedFile.createdBy.id")
    UploadedFileDto convertToDto(UploadedFile uploadedFile);

}
