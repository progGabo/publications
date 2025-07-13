package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.publications.domain.Publication;
import org.publications.service.dto.PublicationCreateDTO;
import org.publications.service.dto.PublicationDTO;

@Mapper(componentModel = "spring", uses = {AuthorInPublicationMapper.class})
public interface PublicationMapper {

    @Mapping(source = "publisher.name", target = "publisher")
    @Mapping(source = "category.name", target = "category")
    @Mapping(source = "language.name", target = "language")
    @Mapping(source = "type.name", target = "type")
    @Mapping(source = "authors", target = "authors")
    PublicationDTO toDto(Publication publication);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "publisher", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "type", ignore = true)
    @Mapping(target = "language", ignore = true)
    Publication toEntity(PublicationCreateDTO dto);
}
