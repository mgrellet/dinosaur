package com.dinosaur.domain.port.out;

import java.util.List;
import java.util.Optional;

import com.dinosaur.domain.model.Dinosaur;

public interface DinosaurPersistencePort {
    Dinosaur save(Dinosaur dinosaur);
    List<Dinosaur> findAll();
    Optional<Dinosaur> findById(Long id);
    void delete(Dinosaur dinosaur);
}
