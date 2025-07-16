package org.publications.service.mapper;

import org.mapstruct.*;
import org.publications.domain.Publication;
import org.publications.service.dto.publications.PublicationSpecificDTO;


@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = AuthorMapper.class)
public interface PublicationSpecificMapper extends EntityMapper<PublicationSpecificDTO, Publication> {

    @Override
    @Mapping(target = "publisherId", source = "publisher.id")
    @Mapping(target = "languageId", source = "language.id")
    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "typeId", source = "type.id")
    @Mapping(target = "authors", source = "authors")
    PublicationSpecificDTO toDto(Publication entity);

    @Override
    @Mapping(source = "publisherId", target = "publisher.id")
    @Mapping(source = "languageId", target = "language.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "typeId", target = "type.id")
    Publication toEntity(PublicationSpecificDTO dto);
}
