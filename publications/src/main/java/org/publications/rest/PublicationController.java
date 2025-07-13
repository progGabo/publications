package org.publications.rest;

import org.publications.service.PublicationService;
import org.publications.service.dto.PublicationCreateDTO;
import org.publications.service.dto.PublicationDTO;
import org.publications.service.dto.PublicationSpecificDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/publications")
public class PublicationController {

    private final PublicationService publicationService;
    private final Logger log = LoggerFactory.getLogger(PublicationController.class);
    public PublicationController(PublicationService publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping
    public ResponseEntity<PublicationCreateDTO> createPublication(@RequestBody PublicationCreateDTO publication) {
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

        return publicationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublicationCreateDTO> updatePublication(@PathVariable Long id,@RequestBody PublicationCreateDTO updated) {
        log.info("REST request to update Publication : {}", updated);

        Optional<PublicationDTO> publicationOpt = publicationService.getById(id);

        if (publicationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(publicationService.save(updated));
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<Page<PublicationSpecificDTO>> getAuthorsPublications(@PathVariable Long id, Pageable pageable){
        return ResponseEntity.ok(publicationService.getAuthorsPublications(pageable, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublication(@PathVariable Long id) {
        log.info("REST request to delete Publication : {}", id);
        publicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
