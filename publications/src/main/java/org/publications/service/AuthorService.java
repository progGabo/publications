package org.publications.service;

import org.publications.domain.Author;
import org.publications.service.dto.AuthorDTO;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

public interface AuthorService {
    @Transactional
    Author register(String nickname, String firstName, String lastName, String rawPassword);

    Author authenticate(String nickname, String rawPassword);

    AuthorDTO save(AuthorDTO authorDTO);

    boolean existsByNickname(String nickname);

    Author getById(Long id);
    List<Author> getAll();

    Optional<Author> findByNickname(String nickname);

    Author update(Long id, Author author);
    void delete(Long id);

    Optional<AuthorDTO> findByFirstNameAndLastName(String firstName, String lastName);
}
