package org.publications.service;

import org.publications.service.dto.PublisherDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PublisherService {
    PublisherDTO save(PublisherDTO publisher);
    Optional<PublisherDTO> getById(Long id);
    Page<PublisherDTO> getAll(Pageable pageable);
    PublisherDTO update(PublisherDTO publisher);
    void delete(Long id);
}
