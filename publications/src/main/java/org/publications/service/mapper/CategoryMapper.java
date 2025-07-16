package org.publications.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.publications.domain.Category;
import org.publications.service.dto.CategoryDTO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    CategoryDTO toDto(Category entity);

    @Override
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    Category toEntity(CategoryDTO dto);
}
