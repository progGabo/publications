package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.Publication;
import org.publications.service.dto.PublicationCreateDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublicationCreateMapper extends EntityMapper<PublicationCreateDTO, Publication> {

    @Mapping(target = "publisherId", source = "publisher.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "typeId", source = "type.id")
    @Mapping(target = "languageId", source = "language.id")
    PublicationCreateDTO toDto(Publication entity);

    @Override
    @Mapping(target = "publisher.id", source = "publisherId")
    @Mapping(target = "type.id", source = "typeId")
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "language.id", source = "languageId")
    Publication toEntity(PublicationCreateDTO dto);
}
