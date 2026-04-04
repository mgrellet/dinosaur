package com.dinosaur.application.port.in;

import com.dinosaur.application.dto.DinosaurResult;

public interface GetDinosaurByIdUseCase {
    DinosaurResult execute(Long id);
}
