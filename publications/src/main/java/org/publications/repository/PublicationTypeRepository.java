package org.publications.repository;

import org.publications.domain.PublicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface PublicationTypeRepository extends JpaRepository<PublicationType, Long> {
}
