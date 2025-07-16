package org.publications.service.impl;


import org.publications.domain.Author;
import org.publications.repository.AuthorRepository;
import org.publications.service.AuthorService;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.mapper.AuthorMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }



    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Author not found with ID: " + id));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }



    @Override
    public Author update(Long id, Author updatedAuthor) {
        Author author = getById(id);
        author.setFirstName(updatedAuthor.getFirstName());
        author.setLastName(updatedAuthor.getLastName());
        return authorRepository.save(author);
    }

    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public Optional<AuthorDTO> findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(firstName, lastName).map(authorMapper::toDto);
    }
}
