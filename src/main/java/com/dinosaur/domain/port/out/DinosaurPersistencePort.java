package com.dinosaur.domain.port.out;

import com.dinosaur.domain.model.Dinosaur;

public interface DinosaurPersistencePort {
    Dinosaur save(Dinosaur dinosaur);
}
