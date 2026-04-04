package com.dinosaur.application.port.in;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;

public interface UpdateDinosaurUseCase {
    DinosaurResult execute(Long id, DinosaurCommand command);
}
