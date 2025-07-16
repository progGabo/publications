package org.publications.service;

import org.publications.service.dto.PublisherDTO;
import org.publications.service.dto.TypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface TypeService {
    TypeDTO save(TypeDTO publisher);
    Optional<TypeDTO> getById(Long id);
    Page<TypeDTO> getAll(Pageable pageable);
    TypeDTO update(TypeDTO publisher);
    void delete(Long id);
}
