package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.service.PublicationService;
import org.publications.service.dto.publications.PublicationDTO;
import org.publications.service.dto.publications.PublicationFilterDTO;
import org.publications.service.dto.publications.PublicationSpecificDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;
    private final Logger log = LoggerFactory.getLogger(PublicationController.class);

    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationSpecificDTO> createPublication(@Valid @RequestBody PublicationSpecificDTO publication) {
        log.info("REST request to save Publication : {}", publication);
        return ResponseEntity.ok(publicationService.save(publication));
    }

    @GetMapping
    public ResponseEntity<Page<PublicationDTO>> getAllPublications(Pageable pageable) {
        log.info("REST request to get a page of Publications");
        return ResponseEntity.ok(publicationService.getAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublication(@PathVariable Long id) {
        log.info("REST request to get Publication : {}", id);
        Optional<PublicationDTO> publicationOpt = publicationService.getById(id);

        return publicationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("@publicationServiceImpl.isOwner(#id, authentication)")
    public ResponseEntity<PublicationSpecificDTO> updatePublication(@PathVariable Long id,@Valid @RequestBody PublicationSpecificDTO updated) {
        log.info("REST request to update Publication : {}", updated);

        Optional<PublicationDTO> publicationOpt = publicationService.getById(id);

        if (publicationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(publicationService.save(updated));
    }

    @GetMapping("filter")
    public ResponseEntity<Page<PublicationDTO>> filterPublications(PublicationFilterDTO filter, Pageable pageable) {
        log.info("Rest request to filter publications: {}", filter);
        return ResponseEntity.ok(publicationService.search(filter, pageable));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@publicationServiceImpl.isOwner(#id, authentication)")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.info("REST request to delete Publication : {}", id);
        Optional<PublicationDTO> publicationOpt = publicationService.getById(id);
        if (publicationOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        publicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
