package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.publications.domain.Publication;
import org.publications.domain.PublicationAuthor;
import org.publications.service.dto.AuthorInPublicationDTO;
import org.publications.service.dto.PublicationCreateDTO;
import org.publications.service.dto.PublicationDTO;

@Mapper(componentModel = "spring", uses = {AuthorInPublicationMapper.class})
public interface AuthorInPublicationMapper {
    @Mapping(source = "author.id", target = "id")
    @Mapping(source = "author.firstName", target = "firstName")
    @Mapping(source = "author.lastName", target = "lastName")
    @Mapping(source = "authorOrder", target = "authorOrder")
    AuthorInPublicationDTO toDto(PublicationAuthor publicationAuthor);
}
