package com.dinosaur.application.service;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.application.port.in.GetDinosaurByIdUseCase;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class GetDinosaurByIdUseCaseImpl implements GetDinosaurByIdUseCase {

    private final DinosaurPersistencePort dinosaurPersistencePort;

    public GetDinosaurByIdUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }

    @Override
    public DinosaurResult execute(Long id) {
        Dinosaur dinosaur = dinosaurPersistencePort.findById(id)
                .orElseThrow(() -> new DinosaurNotFoundException("Dinosaur not found with id: " + id));
        return ApplicationMapper.toResult(dinosaur);
    }
}
