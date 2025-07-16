package org.publications.service;


import org.publications.service.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryService {
    CategoryDTO save(CategoryDTO publisher);
    Optional<CategoryDTO> getById(Long id);
    Page<CategoryDTO> getAll(Pageable pageable);
    CategoryDTO update(CategoryDTO publisher);
    void delete(Long id);
}
