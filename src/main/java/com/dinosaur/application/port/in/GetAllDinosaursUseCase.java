package com.dinosaur.application.port.in;

import java.util.List;

import com.dinosaur.application.dto.DinosaurResult;

public interface GetAllDinosaursUseCase {
    List<DinosaurResult> execute();
}
