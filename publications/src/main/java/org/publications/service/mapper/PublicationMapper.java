package org.publications.service.mapper;

import org.mapstruct.*;
import org.publications.domain.Publication;
import org.publications.service.dto.publications.PublicationDTO;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses =  AuthorMapper.class
)
public interface PublicationMapper extends EntityMapper<PublicationDTO, Publication> {

    @Mapping(source = "publisher.name", target = "publisher")
    @Mapping(source = "category.name",  target = "category")
    @Mapping(source = "language.name",  target = "language")
    @Mapping(source = "type.name",      target = "type")
    PublicationDTO toDto(Publication publication);

    @Override
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "category",  ignore = true)
    @Mapping(target = "language",  ignore = true)
    @Mapping(target = "type",      ignore = true)
    Publication toEntity(PublicationDTO dto);
}