package org.publications.service.impl;

import org.publications.domain.Author;
import org.publications.domain.Publication;
import org.publications.exception.DuplicateIsbnException;
import org.publications.repository.PublicationRepository;
import org.publications.service.AuthorService;
import org.publications.service.PublicationService;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.dto.publications.PublicationDTO;
import org.publications.service.dto.publications.PublicationFilterDTO;
import org.publications.service.dto.publications.PublicationSpecificDTO;
import org.publications.service.mapper.PublicationMapper;
import org.publications.service.mapper.PublicationSpecificMapper;
import org.publications.service.specification.PublicationSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final PublicationSpecificMapper publicationSpecificMapper;
    private final AuthorService authorService;
    private final PublicationMapper publicationMapper;
    private final Logger log = LoggerFactory.getLogger(PublicationServiceImpl.class);

    public PublicationServiceImpl(PublicationMapper publicationMap,
                                  AuthorService authorService,
                                  PublicationRepository publicationRepository,
                                  PublicationSpecificMapper publicationSpecificMapper
    ) {
        this.publicationRepository = publicationRepository;
        this.publicationSpecificMapper = publicationSpecificMapper;
        this.authorService = authorService;
        this.publicationMapper = publicationMap;
    }

    @Override
    public Page<PublicationDTO> search(PublicationFilterDTO filter, Pageable pageable) {
        Specification<Publication> spec = PublicationSpecification.byFilter(filter);
        return publicationRepository.findAll(spec, pageable)
                .map(publicationMapper::toDto);
    }

    @Override
    public PublicationSpecificDTO save(PublicationSpecificDTO dto) {
        if (dto.getId() == null) {
            if (publicationRepository.existsByIsbnIssn(dto.getIsbnIssn())) {
                throw new DuplicateIsbnException(dto.getIsbnIssn());
            }
        }
        else{
            Optional<Publication> existing = publicationRepository.findById(dto.getId());
            if (existing.isPresent() && !existing.get().getIsbnIssn().equals( dto.getIsbnIssn())) {
                if (publicationRepository.existsByIsbnIssn(dto.getIsbnIssn())) {
                    throw new DuplicateIsbnException(dto.getIsbnIssn());
                }
            }
        }

        Publication publication = publicationSpecificMapper.toEntity(dto);

        List<Author> managedAuthors = dto.getAuthors().stream()
                .map(aDto -> {
                    String fn = aDto.getFirstName().trim();
                    String ln = aDto.getLastName().trim();

                    Optional<AuthorDTO> existing = authorService.findByFirstNameAndLastName(fn, ln);

                    Long authorId = existing
                            .map(AuthorDTO::getId)
                            .orElseGet(() -> {
                                AuthorDTO toCreate = new AuthorDTO(null, fn, ln);
                                AuthorDTO created  = authorService.save(toCreate);
                                return created.getId();
                            });

                    return authorService.getById(authorId);
                })
                .collect(Collectors.toList());

        publication.setAuthors(managedAuthors);
        Publication saved = publicationRepository.save(publication);

        return publicationSpecificMapper.toDto(saved);
    }

    @Override
    public Optional<PublicationDTO> getById(Long id) {
        return publicationRepository.findById(id).map(publicationMapper::toDto);
    }

    @Override
    public Page<PublicationDTO> getAll(Pageable pageable) {
        return publicationRepository.findAll(pageable)
                .map(publicationMapper::toDto);
    }

    @Override
    @Transactional
    public PublicationSpecificDTO update(PublicationSpecificDTO updatedPublication) {
        publicationRepository.findById(updatedPublication.getId())
                .orElseThrow(() ->
                        new RuntimeException("Publikácia s ID " + updatedPublication.getId() + " neexistuje."));

        Publication updated = publicationSpecificMapper.toEntity(updatedPublication);
        publicationRepository.save(updated);
        return publicationSpecificMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        publicationRepository.deleteById(id);
    }
}
