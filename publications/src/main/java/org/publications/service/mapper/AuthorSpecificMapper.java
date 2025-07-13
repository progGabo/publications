package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.Author;
import org.publications.service.dto.AuthorSpecificDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthorSpecificMapper extends EntityMapper<AuthorSpecificDTO, Author> {
}
