package org.publications.service.impl;

import org.publications.domain.Category;
import org.publications.domain.Publisher;
import org.publications.repository.CategoryRepository;
import org.publications.repository.PublisherRepository;
import org.publications.service.CategoryService;
import org.publications.service.dto.CategoryDTO;
import org.publications.service.dto.PublisherDTO;
import org.publications.service.mapper.CategoryMapper;
import org.publications.service.mapper.PublisherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        Category category = categoryMapper.toEntity(categoryDTO);
        categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    @Override
    public Optional<CategoryDTO> getById(Long id) {
        return categoryRepository.findById(id).map(categoryMapper::toDto);
    }

    @Override
    public Page<CategoryDTO> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(categoryMapper::toDto);
    }

    @Override
    public CategoryDTO update(CategoryDTO publisher) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
