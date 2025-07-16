package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.PublicationType;
import org.publications.service.dto.TypeDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TypeMapper extends EntityMapper<TypeDTO, PublicationType> {

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    PublicationType toEntity(TypeDTO dto);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    TypeDTO toDto(PublicationType entity);
}
