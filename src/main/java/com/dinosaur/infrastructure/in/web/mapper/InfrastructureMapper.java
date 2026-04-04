package com.dinosaur.infrastructure.in.web.mapper;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.dto.DinosaurResponse;
import com.dinosaur.infrastructure.out.persistance.DinosaurJpaEntity;

public class InfrastructureMapper {

    public static DinosaurResponse toResponse(DinosaurResult result) {
        return new DinosaurResponse(result.id(), result.name(), result.species(), result.discoveryDate(),
                result.extinctionDate(), result.status());
    }

    public static DinosaurCommand toCommand(DinosaurRequest request) {
        return new DinosaurCommand(request.name(), request.species(), request.discoveryDate(), request.extinctionDate(),
                request.status());
    }

    public static DinosaurJpaEntity toEntity(Dinosaur dinosaur) {
        return new DinosaurJpaEntity(dinosaur.getId(), dinosaur.getName(), dinosaur.getSpecies(), dinosaur.getDiscoveryDate(),
                dinosaur.getExtinctionDate(), dinosaur.getStatus());
    }

    public static Dinosaur toDomain(DinosaurJpaEntity savedEntity) {
        return new Dinosaur(savedEntity.getId(), savedEntity.getName(), savedEntity.getSpecies(),
                savedEntity.getDiscoveryDate(), savedEntity.getExtinctionDate(), savedEntity.getStatus());
    }
}
