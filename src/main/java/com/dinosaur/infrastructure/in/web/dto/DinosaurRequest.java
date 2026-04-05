package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DinosaurRequest(
    @NotBlank(message = "Name is mandatory")
    String name,
    @NotBlank(message = "Species is mandatory")
    String species,
    @NotNull(message = "Discovery date is mandatory")
    LocalDateTime discoveryDate,
    @NotNull(message = "Extinction date is mandatory")
    LocalDateTime extinctionDate,
    String status
) {}