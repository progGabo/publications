package org.publications.repository;


import org.publications.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByLastNameContainingIgnoreCase(String lastName);
    Optional<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);
    Optional<Author> findByNickname(String nickname);
    boolean existsByNickname(String nickname);
}