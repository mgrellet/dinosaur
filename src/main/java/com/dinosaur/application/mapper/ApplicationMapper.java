package com.dinosaur.application.mapper;

import static java.util.Objects.isNull;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;

public class ApplicationMapper {

    public static Dinosaur toDomain(DinosaurCommand command) {
        return new Dinosaur(command.name(), command.species(), command.discoveryDate(), command.extinctionDate(),
                parseStatus(command.status()));
    }

    public static DinosaurResult toResult(Dinosaur dinosaur) {
        return new DinosaurResult(dinosaur.getId(), dinosaur.getName(), dinosaur.getSpecies(), dinosaur.getDiscoveryDate(),
                dinosaur.getExtinctionDate(), dinosaur.getStatus().name());
    }

    static DinosaurStatus parseStatus(String status) {
        if (isNull(status) || status.isBlank()) {
            return null;
        }
        return DinosaurStatus.valueOf(status.trim());
    }
}
