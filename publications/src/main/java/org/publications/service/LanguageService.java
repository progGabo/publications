package org.publications.service;

import org.publications.service.dto.LanguageDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LanguageService {
    LanguageDTO save(LanguageDTO language);
    Optional<LanguageDTO> getById(Long id);
    Page<LanguageDTO> getAll(Pageable pageable);
    LanguageDTO update(LanguageDTO language);
    void delete(Long id);
}
