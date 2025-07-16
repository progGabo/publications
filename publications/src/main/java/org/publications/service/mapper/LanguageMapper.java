package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.Language;
import org.publications.service.dto.LanguageDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {
    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    LanguageDTO toDto(Language entity);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Language toEntity(LanguageDTO dto);
}
