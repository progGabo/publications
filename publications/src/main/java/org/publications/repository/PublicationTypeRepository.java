package org.publications.repository;

import org.publications.domain.PublicationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(path="type")
public interface PublicationTypeRepository extends JpaRepository<PublicationType, Long> {
}
