package org.publications.service.impl;


import org.publications.domain.Author;
import org.publications.repository.AuthorRepository;
import org.publications.service.AuthorService;
import org.publications.service.dto.AuthorDTO;
import org.publications.service.mapper.AuthorMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorServiceImpl implements UserDetailsService, AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final PasswordEncoder encoder;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapper authorMapper, PasswordEncoder encoder) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.encoder = encoder;
    }

    @Transactional
    @Override
    public Author register(String nickname, String firstName, String lastName, String rawPassword) {
        if (authorRepository.existsByNickname(nickname)) {
            throw new RuntimeException("Nickname už existuje: " + nickname);
        }
        Author a = new Author();
        a.setNickname(nickname);
        a.setFirstName(firstName);
        a.setLastName(lastName);
        a.setPass( encoder.encode(rawPassword) );
        return authorRepository.save(a);
    }

    @Override
    public Author authenticate(String nickname, String rawPassword) {
        Author a = authorRepository.findByNickname(nickname)
                .orElseThrow(() -> new UsernameNotFoundException("Používateľ neexistuje"));
        if (!encoder.matches(rawPassword, a.getPass())) {
            throw new BadCredentialsException("Nesprávne heslo");
        }
        return a;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author a = authorRepository.findByNickname(username)
                .orElseThrow(() -> new UsernameNotFoundException("Používateľ neexistuje"));
        // všetci majú rolu USER
        return User.builder()
                .username(a.getNickname())
                .password(a.getPass())
                .roles("USER")
                .build();
    }

    @Override
    public AuthorDTO save(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        authorRepository.save(author);
        return authorMapper.toDto(author);
    }
    
    @Override
    public boolean existsByNickname(String nickname) {
        return authorRepository.existsByNickname(nickname);
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
    public Optional<Author> findByNickname(String nickname) {
        return authorRepository.findByNickname(nickname);
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
