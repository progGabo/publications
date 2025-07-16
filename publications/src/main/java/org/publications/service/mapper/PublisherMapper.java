package org.publications.service.mapper;

import org.mapstruct.*;
import org.publications.domain.Publisher;
import org.publications.service.dto.PublisherDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface PublisherMapper extends EntityMapper<PublisherDTO , Publisher> {

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    PublisherDTO toDto(Publisher entity);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Publisher toEntity(PublisherDTO dto);
}
