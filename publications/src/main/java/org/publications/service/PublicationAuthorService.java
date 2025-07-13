package org.publications.service;

import org.publications.domain.PublicationAuthor;

import java.util.List;

public interface PublicationAuthorService {
    PublicationAuthor save(PublicationAuthor author);
    PublicationAuthor getById(Long id);
    List<PublicationAuthor> getAll();
    PublicationAuthor update(PublicationAuthor author);
    void delete(Long id);
}
