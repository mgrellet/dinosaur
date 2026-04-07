package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(name = "DinosaurRequest", description = "Datos de entrada para crear o actualizar un dinosaurio")
public record DinosaurRequest(
        @Schema(description = "Nombre común o descriptivo", example = "Tyrannosaurus Rex")
        @NotBlank(message = "Name is mandatory")
        String name,
        @Schema(description = "Especie o clasificación", example = "Theropod")
        @NotBlank(message = "Species is mandatory")
        String species,
        @Schema(description = "Fecha de descubrimiento (ISO-8601)", example = "1902-01-01T23:59:59")
        @NotNull(message = "Discovery date is mandatory")
        LocalDateTime discoveryDate,
        @Schema(description = "Fecha de extinción (ISO-8601)", example = "2023-12-31T23:59:59")
        @NotNull(message = "Extinction date is mandatory")
        LocalDateTime extinctionDate,
        @Schema(description = "Estado; opcional en creación", allowableValues = { "ALIVE", "ENDANGERED",
                "EXTINCT" }, example = "ALIVE")
        String status
) {
}