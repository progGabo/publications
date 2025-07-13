package org.publications.service;

import org.publications.domain.Author;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.dto.AuthorSpecificDTO;


import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorDTO save(AuthorDTO authorDTO);
    Author getById(Long id);
    List<Author> getAll();
    Author update(Long id, Author author);
    void delete(Long id);

    Optional<AuthorSpecificDTO> findByFirstNameAndLastName(String firstName, String lastName);
}
