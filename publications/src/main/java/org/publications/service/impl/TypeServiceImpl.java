package org.publications.service.impl;


import org.publications.domain.PublicationType;
import org.publications.domain.Publisher;
import org.publications.repository.PublicationTypeRepository;
import org.publications.repository.PublisherRepository;
import org.publications.service.TypeService;
import org.publications.service.dto.PublisherDTO;
import org.publications.service.dto.TypeDTO;
import org.publications.service.mapper.PublisherMapper;
import org.publications.service.mapper.TypeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {
    private final PublicationTypeRepository typeRepository;
    private final TypeMapper typeMapper;

    public TypeServiceImpl(PublicationTypeRepository typeRepository, TypeMapper typeMapper) {
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }

    @Override
    public TypeDTO save(TypeDTO typeDTO) {
        PublicationType type = typeMapper.toEntity(typeDTO);
        typeRepository.save(type);
        return typeMapper.toDto(type);
    }

    @Override
    public Optional<TypeDTO> getById(Long id) {
        return typeRepository.findById(id).map(typeMapper::toDto);
    }

    @Override
    public Page<TypeDTO> getAll(Pageable pageable) {
        return typeRepository.findAll(pageable)
                .map(typeMapper::toDto);
    }

    @Override
    public TypeDTO update(TypeDTO typeDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
