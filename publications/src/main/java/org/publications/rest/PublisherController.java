package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.service.PublisherService;
import org.publications.service.dto.PublisherDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/publisher")
public class PublisherController {
    private final PublisherService publisherService;
    private final Logger log = LoggerFactory.getLogger(PublisherController.class);

    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostMapping
    public ResponseEntity<PublisherDTO> createPublisher(@Valid @RequestBody PublisherDTO publisherRequest) {
        log.info("REST request to save Publisher : {}", publisherRequest);
        PublisherDTO publisherDTO = publisherService.save(publisherRequest);
        return ResponseEntity.ok(publisherDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherDTO> getPublisher(@PathVariable Long id) {
        log.info("REST request to get Publisher : {}", id);
        Optional<PublisherDTO> publisherOpt = publisherService.getById(id);

        return publisherOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<PublisherDTO>> getAllPublishers(Pageable pageable) {
        log.info("REST request to get a page of Publishers");
        return ResponseEntity.ok(publisherService.getAll(pageable));
    }
}
