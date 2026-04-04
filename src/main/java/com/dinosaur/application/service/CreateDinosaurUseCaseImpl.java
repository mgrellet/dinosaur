package com.dinosaur.application.service;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class CreateDinosaurUseCaseImpl implements CreateDinosaurUseCase {

    private final DinosaurPersistencePort dinosaurPersistencePort;
    public CreateDinosaurUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }

    @Override
    public DinosaurResult execute(DinosaurCommand command) {

        Dinosaur dinosaur = ApplicationMapper.toDomain(command);

        Dinosaur savedDinosaur = dinosaurPersistencePort.save(dinosaur);

        return ApplicationMapper.toResult(savedDinosaur);
    }

}
