package com.dinosaur.application.service;

import java.util.List;
import java.util.stream.Collectors;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.mapper.ApplicationMapper;
import com.dinosaur.application.port.in.GetAllDinosaursUseCase;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class GetAllDinosaursUseCaseImpl implements GetAllDinosaursUseCase {

    private final DinosaurPersistencePort dinosaurPersistencePort;

    public GetAllDinosaursUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
    }

    @Override
    public List<DinosaurResult> execute() {
        List<Dinosaur> dinosaurs = dinosaurPersistencePort.findAll();
        return dinosaurs.stream().map(ApplicationMapper::toResult).collect(Collectors.toList());
    }
    
}
