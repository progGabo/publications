package org.publications.service.impl;

import org.publications.domain.PublicationAuthor;
import org.publications.service.PublicationAuthorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PublicationAuthorServiceImpl implements PublicationAuthorService {



    @Override
    public PublicationAuthor save(PublicationAuthor author) {
        return null;
    }

    @Override
    public PublicationAuthor getById(Long id) {
        return null;
    }

    @Override
    public List<PublicationAuthor> getAll() {
        return List.of();
    }

    @Override
    public PublicationAuthor update(PublicationAuthor author) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
