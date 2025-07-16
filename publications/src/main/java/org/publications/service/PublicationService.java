package org.publications.service;

import org.publications.service.dto.publications.PublicationDTO;
import org.publications.service.dto.publications.PublicationFilterDTO;
import org.publications.service.dto.publications.PublicationSpecificDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PublicationService {
    Page<PublicationDTO> search(PublicationFilterDTO filter, Pageable pageable);
    PublicationSpecificDTO save(PublicationSpecificDTO publicationDTO);
    Optional<PublicationDTO> getById(Long id);
    Page<PublicationDTO> getAll(Pageable pageable);
    PublicationSpecificDTO update(PublicationSpecificDTO publication);
    void delete(Long id);
}
