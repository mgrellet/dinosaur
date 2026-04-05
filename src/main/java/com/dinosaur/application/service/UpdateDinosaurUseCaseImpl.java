package com.dinosaur.application.service;

import java.time.LocalDateTime;

import static java.util.Objects.isNull;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.domain.exception.DinosaurAlreadyExistsException;
import com.dinosaur.domain.exception.DinosaurInvalidDateException;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.exception.InvalidDinosaurException;
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

        DinosaurStatus validatedDinosaurStatus = validateStatus(command.status());
        validateExistingDinosaur(command.name(), id);
        validateDinosaurDate(command.discoveryDate(), command.extinctionDate());

        Dinosaur dinosaur = dinosaurPersistencePort.findById(id)
                .orElseThrow(() -> new DinosaurNotFoundException("Dinosaur not found with id: " + id));

        validateIfDinosaurIsExtinct(dinosaur.getStatus());

        dinosaur.setName(command.name());
        dinosaur.setSpecies(command.species());
        dinosaur.setDiscoveryDate(command.discoveryDate());
        dinosaur.setExtinctionDate(command.extinctionDate());
        dinosaur.setStatus(validatedDinosaurStatus);
        Dinosaur updatedDinosaur = dinosaurPersistencePort.save(dinosaur);
        return ApplicationMapper.toResult(updatedDinosaur);
    }

    private DinosaurStatus validateStatus(String status) {
        if (isNull(status)) {
            throw new InvalidDinosaurException("Dinosaur status is required");
        }
        String trimmedStatusString = status.trim();
        if (trimmedStatusString.isEmpty()) {
            throw new InvalidDinosaurException("Dinosaur status cannot be blank");
        }
        try {
            return DinosaurStatus.valueOf(trimmedStatusString);
        } catch (IllegalArgumentException e) {
            throw new InvalidDinosaurException("Invalid dinosaur status: " + trimmedStatusString);
        }
    }

    private void validateExistingDinosaur(String name, Long id) {
        if (dinosaurPersistencePort.existsByNameAndIdNot(name, id)) {
            throw new DinosaurAlreadyExistsException("Dinosaur with name " + name + " already exists");
        }
    }
    
    private void validateDinosaurDate(LocalDateTime discoveryDate, LocalDateTime extinctionDate) {
        if (discoveryDate.isAfter(extinctionDate) || discoveryDate.isEqual(extinctionDate)) {
            throw new DinosaurInvalidDateException("Discovery date cannot be after or equal to extinction date");
        }
    }

    private void validateIfDinosaurIsExtinct(DinosaurStatus status) {
        if (status == DinosaurStatus.EXTINCT) {
            throw new InvalidDinosaurException("Cannot update a extinct dinosaur");
        }
    }
}
