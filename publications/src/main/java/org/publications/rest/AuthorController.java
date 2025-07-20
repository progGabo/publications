package org.publications.rest;

import jakarta.validation.Valid;
import org.publications.domain.Author;
import org.publications.security.JwtTokenProvider;
import org.publications.service.AuthorService;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.dto.LoginRequestDTO;
import org.publications.service.dto.RegisterRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private final JwtTokenProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${app.jwtExpirationMs}")
    private long jwtExpirationMs;

    public AuthorController(AuthorService authorService, JwtTokenProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.authorService = authorService;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO req) {
        if (authorService.existsByNickname(req.getNickname())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Nickname už existuje"));
        }

        Author saved = authorService.register(
                req.getNickname(),
                req.getFirstName(),
                req.getLastName(),
                req.getPassword()
        );

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(Map.of(
                        "id",       saved.getId(),
                        "nickname", saved.getNickname()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO req) {
        try {

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            req.getNickname(),
                            req.getPassword()
                    )
            );

            String token = jwtProvider.createToken(auth);

            ResponseCookie jwtCookie = ResponseCookie.from("JWT", token)
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(jwtExpirationMs/1000)
                    .sameSite("Strict")
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                    .body(Map.of("status", "ok"));
        } catch (BadCredentialsException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Neplatné prihlasovacie údaje"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthorDTO> getCurrentAuthor(Authentication authentication) {
        String nickname = authentication.getName();

        Author author = authorService.findByNickname(nickname)
                .orElseThrow(() -> new IllegalArgumentException("Neexistujúci používateľ: " + nickname));

        AuthorDTO dto = new AuthorDTO(
                author.getId(),
                author.getFirstName(),
                author.getLastName()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication) {
        ResponseCookie deleteJwtCookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deleteJwtCookie.toString())
                .build();
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