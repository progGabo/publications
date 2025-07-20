package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.service.TypeService;
import org.publications.service.dto.TypeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/type")
public class TypeController {
    private final TypeService typeService;
    private final Logger log = LoggerFactory.getLogger(TypeController.class);
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    @PostMapping
    public ResponseEntity<TypeDTO> createType(@Valid @RequestBody TypeDTO typeRequest) {
        log.info("REST request to save Type : {}", typeRequest);
        TypeDTO typeDTO = typeService.save(typeRequest);
        return ResponseEntity.ok(typeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDTO> getType(@PathVariable Long id) {
        log.info("REST request to get Type : {}", id);
        Optional<TypeDTO> typeOpt = typeService.getById(id);
        return typeOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<TypeDTO>> getAllTypes(Pageable pageable) {
        log.info("REST request to get a page of Type");
        return ResponseEntity.ok(typeService.getAll(pageable));
    }

}
