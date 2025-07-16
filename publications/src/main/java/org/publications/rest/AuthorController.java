package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.domain.Author;
import org.publications.service.AuthorService;
import org.publications.service.dto.AuthorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final Logger log = LoggerFactory.getLogger(AuthorController.class);

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody @Valid AuthorDTO author) {
        log.info("REST request to save Author : {}", author);
        AuthorDTO authorCreateDTO = authorService.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(authorCreateDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<AuthorDTO> findByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        log.info("REST request to find Author by firstName : {} and lastName : {}", firstName, lastName);
        Optional<AuthorDTO> author = authorService.findByFirstNameAndLastName(firstName, lastName);
        return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        log.info("Request to get Author : {}", id);
        return ResponseEntity.ok(authorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        log.info("REST request to get all Authors");
        return ResponseEntity.ok(authorService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        log.info("REST request to update Author : {}", author);
        return ResponseEntity.ok(authorService.update(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        log.info("REST request to delete Author : {}", id);
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}