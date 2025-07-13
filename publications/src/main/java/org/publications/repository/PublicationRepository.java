package org.publications.repository;

import org.publications.domain.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

    @Query("SELECT p FROM Publication p JOIN p.authors pa WHERE pa.author.id = :id")
    Page<Publication> findPublicationsByAuthor(Pageable pageable, @Param("id") Long id);

    @EntityGraph(attributePaths = {"authors", "authors.author", "publisher","language", "category", "type"})
    @Query("SELECT p FROM Publication p")
    Page<Publication> findAllWithAuthors(Pageable pageable);
}
