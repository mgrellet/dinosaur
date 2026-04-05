package com.dinosaur.infrastructure.out.persistance;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;
import com.dinosaur.infrastructure.in.web.mapper.InfrastructureMapper;

@Component
public class DinosaurPersistenceAdapter implements DinosaurPersistencePort {

    private final DinosaurJpaRepository repository;

    public DinosaurPersistenceAdapter(DinosaurJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dinosaur save(Dinosaur dinosaur) {
        DinosaurJpaEntity entity = InfrastructureMapper.toEntity(dinosaur);
        DinosaurJpaEntity savedEntity = repository.save(entity);
        return InfrastructureMapper.toDomain(savedEntity);
    }

    @Override
    public List<Dinosaur> findAll() {
        List<DinosaurJpaEntity> entities = repository.findAll();
        return entities.stream().map(InfrastructureMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Dinosaur> findById(Long id) {
        return repository.findById(id).map(InfrastructureMapper::toDomain);
    }

    @Override
    public void delete(Dinosaur dinosaur) {
        DinosaurJpaEntity entity = InfrastructureMapper.toEntity(dinosaur);
        repository.delete(entity);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Long id) {
        return repository.existsByNameIgnoreCaseAndIdNot(name, id);
    }
}
