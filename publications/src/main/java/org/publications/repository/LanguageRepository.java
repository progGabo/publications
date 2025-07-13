package org.publications.repository;

import org.publications.domain.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(path = "language")
public interface LanguageRepository extends JpaRepository<Language, Long> {
}
