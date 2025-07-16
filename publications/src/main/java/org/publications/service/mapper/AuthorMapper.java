package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.Author;
import org.publications.service.dto.AuthorDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Override
    AuthorDTO toDto(Author entity);

    @Override
    Author toEntity(AuthorDTO dto);
}
