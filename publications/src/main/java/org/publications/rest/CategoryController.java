package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.service.CategoryService;
import org.publications.service.dto.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;
    private final Logger log = LoggerFactory.getLogger(CategoryController.class);
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@Valid @RequestBody CategoryDTO categoryRequest) {
        log.info("REST request to save Category : {}", categoryRequest);
        CategoryDTO categoryDTO = categoryService.save(categoryRequest);
        return ResponseEntity.ok(categoryDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable Long id) {
        log.info("REST request to get Category : {}", id);
        Optional<CategoryDTO> categoryOpt = categoryService.getById(id);

        return categoryOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAllCategories(Pageable pageable) {
        log.info("REST request to get a page of Category");
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }
}
