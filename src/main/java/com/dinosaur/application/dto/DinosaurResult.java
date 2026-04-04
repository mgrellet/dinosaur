package com.dinosaur.application.dto;

import java.time.LocalDateTime;

public record DinosaurResult(
    Long id,
    String name,
    String species,
    LocalDateTime discoveryDate,
    LocalDateTime extinctionDate,
    String status
) {
}

