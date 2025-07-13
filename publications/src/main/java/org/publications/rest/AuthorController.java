package org.publications.rest;

import org.publications.domain.Author;
import org.publications.domain.PublicationAuthor;
import org.publications.repository.PublicationAuthorRepository;
import org.publications.service.AuthorService;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.dto.AuthorSpecificDTO;
//import org.publications.service.mapper.AuthorPublicationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final PublicationAuthorRepository publicationAuthorRepository;
    //private final AuthorPublicationsMapper authorPublicationsMapper;

    public AuthorController(AuthorService authorService, PublicationAuthorRepository publicationAuthorRepository
                            ) {
        this.authorService = authorService;
        this.publicationAuthorRepository = publicationAuthorRepository;
        //this.authorPublicationsMapper = authorPublicationsMapper;
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody AuthorDTO author) {
        AuthorDTO authorCreateDTO = authorService.save(author);
        log.info("Created autor return: " + authorCreateDTO.toString());
        return ResponseEntity.ok(authorCreateDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<AuthorSpecificDTO> findByFirstAndLastName(@RequestParam String firstName, @RequestParam String lastName) {
        Optional<AuthorSpecificDTO> author = authorService.findByFirstNameAndLastName(firstName, lastName);
        return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
        log.info("Request to get Author : {}", id);
        return ResponseEntity.ok(authorService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        return ResponseEntity.ok(authorService.update(id, author));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.noContent().build();
    }

}