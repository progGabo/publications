package org.publications.service.impl;

import org.publications.domain.Publisher;
import org.publications.repository.PublisherRepository;
import org.publications.service.PublisherService;
import org.publications.service.dto.PublisherDTO;
import org.publications.service.mapper.PublisherMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public PublisherServiceImpl(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    @Override
    public PublisherDTO save(PublisherDTO publisher) {
        Publisher publisher1 = publisherMapper.toEntity(publisher);
        publisherRepository.save(publisher1);
        return publisherMapper.toDto(publisher1);
    }

    @Override
    public Optional<PublisherDTO> getById(Long id) {
        return publisherRepository.findById(id).map(publisherMapper::toDto);
    }

    @Override
    public Page<PublisherDTO> getAll(Pageable pageable) {
        return publisherRepository.findAll(pageable)
                .map(publisherMapper::toDto);
    }

    @Override
    public PublisherDTO update(PublisherDTO publisher) {
        return null;
    }

    @Override
    public void delete(Long id) {
    }
}
