package org.publications.service.impl;

import org.publications.domain.Author;
import org.publications.domain.Publication;
import org.publications.domain.PublicationAuthor;
import org.publications.domain.embeddedIds.PublicationAuthorId;
import org.publications.repository.PublicationRepository;
import org.publications.rest.PublicationController;
import org.publications.service.AuthorService;
import org.publications.service.PublicationService;
import org.publications.service.dto.PublicationCreateDTO;
import org.publications.service.dto.PublicationDTO;
import org.publications.service.dto.PublicationSpecificDTO;
import org.publications.service.mapper.PublicationCreateMapper;
import org.publications.service.mapper.PublicationMapper;
import org.publications.service.mapper.PublicationSpecificMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;
    private final PublicationCreateMapper publicationCreateMapper;
    private final PublicationSpecificMapper publicationSpecificMapper;
    private final AuthorService authorService;
    private final PublicationMapper publicationMapper;
    private final Logger log = LoggerFactory.getLogger(PublicationServiceImpl.class);

    public PublicationServiceImpl(PublicationMapper publicationMap, AuthorService authorService, PublicationRepository publicationRepository, PublicationCreateMapper publicationMapper, PublicationSpecificMapper publicationSpecificMapper) {
        this.publicationRepository = publicationRepository;
        this.publicationCreateMapper = publicationMapper;
        this.publicationSpecificMapper = publicationSpecificMapper;
        this.authorService = authorService;
        this.publicationMapper = publicationMap;
    }

    @Override
    public Page<PublicationSpecificDTO> getAuthorsPublications(Pageable pageable, Long id){
        return publicationRepository.findPublicationsByAuthor(pageable, id).map(publicationSpecificMapper::toDto);
    }

    @Override
    public PublicationCreateDTO save(PublicationCreateDTO publicationDto) {
        Publication publication = publicationCreateMapper.toEntity(publicationDto);
        publication.setAuthors(new ArrayList<>());

        final Publication savedPublication = publicationRepository.save(publication);

        List<PublicationAuthor> publicationAuthors = publicationDto.getAuthors().stream()
                .map(authorDto -> {
                    Author author = authorService.getById(authorDto.getAuthorId());

                    PublicationAuthor pa = new PublicationAuthor();
                    pa.setAuthor(author);
                    pa.setPublication(savedPublication);

                    PublicationAuthorId id = new PublicationAuthorId();
                    id.setAuthorId(author.getId());
                    id.setPublicationId(savedPublication.getId());
                    pa.setId(id);

                    pa.setAuthorOrder(authorDto.getAuthorOrder());
                    return pa;
                })
                .collect(Collectors.toList());

        publication.setAuthors(publicationAuthors);
        publication = publicationRepository.save(publication);

        return publicationCreateMapper.toDto(publication);
    }

    @Override
    public Optional<PublicationDTO> getById(Long id) {
        return publicationRepository.findById(id).map(publicationMapper::toDto);
    }

    @Override
    public Page<PublicationDTO> getAll(Pageable pageable) {
        return publicationRepository.findAllWithAuthors(pageable)
                .map(publicationMapper::toDto);
    }

    @Override
    @Transactional
    public PublicationCreateDTO update(PublicationCreateDTO updatedPublication) {
        publicationRepository.findById(updatedPublication.getId())
                .orElseThrow(() ->
                        new RuntimeException("Publikácia s ID " + updatedPublication.getId() + " neexistuje."));

        Publication updated = publicationCreateMapper.toEntity(updatedPublication);
        publicationRepository.save(updated);
        return publicationCreateMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        publicationRepository.deleteById(id);
    }
}
