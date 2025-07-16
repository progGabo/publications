package org.publications.repository;

import org.publications.domain.Publication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long>,
                                               JpaSpecificationExecutor<Publication> {

    @EntityGraph(attributePaths = {"authors", "publisher", "category", "language", "type"})
    Page<Publication> findAll(Pageable pageable);

    boolean existsByIsbnIssn(String isbnIssn);
}
