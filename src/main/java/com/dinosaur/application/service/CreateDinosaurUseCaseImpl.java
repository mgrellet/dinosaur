package com.dinosaur.application.service;

import java.time.LocalDateTime;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.domain.exception.DinosaurAlreadyExistsException;
import com.dinosaur.domain.exception.DinosaurInvalidDateException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class CreateDinosaurUseCaseImpl implements CreateDinosaurUseCase {

    private final DinosaurPersistencePort dinosaurPersistencePort;

    public CreateDinosaurUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }

    @Override
    public DinosaurResult execute(DinosaurCommand command) {

        validateExistingDinosaur(command.name());

        validateDinosaurDate(command.discoveryDate(), command.extinctionDate());

        Dinosaur dinosaur = ApplicationMapper.toDomain(command);

        Dinosaur savedDinosaur = dinosaurPersistencePort.save(dinosaur);

        return ApplicationMapper.toResult(savedDinosaur);
    }

    private void validateExistingDinosaur(String name) {
        if (dinosaurPersistencePort.existsByName(name)) {
            throw new DinosaurAlreadyExistsException("Dinosaur with name " + name + " already exists");
        }
    }

    private void validateDinosaurDate(LocalDateTime discoveryDate, LocalDateTime extinctionDate) {
        if (discoveryDate.isAfter(extinctionDate) || discoveryDate.isEqual(extinctionDate)) {
            throw new DinosaurInvalidDateException("Discovery date cannot be after or equal to extinction date");
        }
    }

}
