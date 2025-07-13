package org.publications.repository;

import org.publications.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:4200")
@RepositoryRestResource(path = "category")
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
