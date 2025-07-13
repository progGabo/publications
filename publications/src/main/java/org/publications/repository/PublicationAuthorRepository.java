package org.publications.repository;

import org.publications.domain.PublicationAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PublicationAuthorRepository extends JpaRepository<PublicationAuthor, Long> {
    List<PublicationAuthor> findAllByAuthorId(Long id);
}
