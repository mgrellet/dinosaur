package com.dinosaur.infrastructure.out.persistance;

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
    
}
