package com.dinosaur.application.service;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class UpdateDinosaurUseCaseImpl implements UpdateDinosaurUseCase {
    private final DinosaurPersistencePort dinosaurPersistencePort;

    public UpdateDinosaurUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }
    
    @Override
    public DinosaurResult execute(Long id, DinosaurCommand command) {
        Dinosaur dinosaur = dinosaurPersistencePort.findById(id)
                .orElseThrow(() -> new DinosaurNotFoundException("Dinosaur not found with id: " + id));
        dinosaur.setName(command.name());
        dinosaur.setSpecies(command.species());
        dinosaur.setDiscoveryDate(command.discoveryDate());
        dinosaur.setExtinctionDate(command.extinctionDate());
        dinosaur.setStatus(DinosaurStatus.valueOf(command.status()));
        Dinosaur updatedDinosaur = dinosaurPersistencePort.save(dinosaur);
        return ApplicationMapper.toResult(updatedDinosaur);
    }
}
