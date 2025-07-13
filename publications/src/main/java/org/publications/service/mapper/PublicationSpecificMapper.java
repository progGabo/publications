package org.publications.service.mapper;

import org.mapstruct.*;
import org.publications.domain.Publication;
import org.publications.service.dto.PublicationSpecificDTO;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublicationSpecificMapper extends EntityMapper<PublicationSpecificDTO, Publication> {

    @Override
    @Mapping(target = "publisher", source = "publisher.name")
    @Mapping(target = "language", source = "language.name")
    @Mapping(target = "category", source = "category.name")
    @Mapping(target = "type", source = "type.name")
    PublicationSpecificDTO toDto(Publication entity);

    @Override
    @Mapping(source = "publisher", target = "publisher.name")
    @Mapping(source = "language", target = "language.name")
    @Mapping(source = "category", target = "category.name")
    @Mapping(source = "type", target = "type.name")
    Publication toEntity(PublicationSpecificDTO dto);
}
