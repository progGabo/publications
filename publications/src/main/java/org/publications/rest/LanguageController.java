package org.publications.rest;

import org.publications.service.LanguageService;
import org.publications.service.dto.LanguageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/language")
public class LanguageController {
    private final LanguageService languageService;
    private final Logger log = LoggerFactory.getLogger(LanguageController.class);

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    @PostMapping
    public ResponseEntity<LanguageDTO> createLanguage(@RequestBody LanguageDTO languageRequest) {
        log.info("REST request to save Language : {}", languageRequest);
        LanguageDTO languageDTO = languageService.save(languageRequest);
        return ResponseEntity.ok(languageDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LanguageDTO> getLanguage(@PathVariable Long id) {
        log.info("REST request to get Language : {}", id);
        Optional<LanguageDTO> languageOpt = languageService.getById(id);

        return languageOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<LanguageDTO>> getAllLanguages(Pageable pageable) {
        log.info("REST request to get a page of Languages");
        return ResponseEntity.ok(languageService.getAll(pageable));
    }
}
