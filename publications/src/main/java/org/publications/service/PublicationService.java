package org.publications.service;

import org.publications.domain.Publication;
import org.publications.service.dto.PublicationCreateDTO;
import org.publications.service.dto.PublicationDTO;
import org.publications.service.dto.PublicationSpecificDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PublicationService {
    PublicationCreateDTO save(PublicationCreateDTO publicationDTO);
    Optional<PublicationDTO> getById(Long id);
    Page<PublicationSpecificDTO> getAuthorsPublications(Pageable pageable, Long id);
    Page<PublicationDTO> getAll(Pageable pageable);
    PublicationCreateDTO update(PublicationCreateDTO publication);
    void delete(Long id);
}
