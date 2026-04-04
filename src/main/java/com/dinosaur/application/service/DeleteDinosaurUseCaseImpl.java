package com.dinosaur.application.service;

import com.dinosaur.application.port.in.DeleteDinosaurUseCase;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class DeleteDinosaurUseCaseImpl implements DeleteDinosaurUseCase {
    private final DinosaurPersistencePort dinosaurPersistencePort;

    public DeleteDinosaurUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }

    @Override
    public void execute(Long id) {
        Dinosaur dinosaur = dinosaurPersistencePort.findById(id)
                .orElseThrow(() -> new DinosaurNotFoundException("Dinosaur not found with id: " + id));
        dinosaurPersistencePort.delete(dinosaur);
    }
}
