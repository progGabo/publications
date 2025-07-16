package org.publications.service.impl;

import org.publications.domain.Language;
import org.publications.repository.LanguageRepository;
import org.publications.service.LanguageService;
import org.publications.service.dto.LanguageDTO;
import org.publications.service.mapper.LanguageMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;

    public LanguageServiceImpl(LanguageRepository languageRepository, LanguageMapper languageMapper) {
        this.languageRepository = languageRepository;
        this.languageMapper = languageMapper;
    }


    @Override
    public LanguageDTO save(LanguageDTO languageDTO) {
        Language language = languageMapper.toEntity(languageDTO);
        languageRepository.save(language);
        return languageMapper.toDto(language);
    }

    @Override
    public Optional<LanguageDTO> getById(Long id) {
        return languageRepository.findById(id).map(languageMapper::toDto);
    }

    @Override
    public Page<LanguageDTO> getAll(Pageable pageable) {
        return languageRepository.findAll(pageable)
                .map(languageMapper::toDto);
    }

    @Override
    public LanguageDTO update(LanguageDTO language) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
