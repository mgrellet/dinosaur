package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "DinosaurResponse", description = "Representación de un dinosaurio expuesta por la API")
public class DinosaurResponse {
    @Schema(description = "Identificador único", example = "1")
    Long id;
    @Schema(example = "Tyrannosaurus Rex")
    String name;
    @Schema(example = "Theropod")
    String species;
    @Schema(description = "Fecha de descubrimiento", example = "1902-01-01T23:59:59")
    LocalDateTime discoveryDate;
    @Schema(description = "Fecha de extinción", example = "2023-12-31T23:59:59")
    LocalDateTime extinctionDate;
    @Schema(description = "Estado actual", allowableValues = { "ALIVE", "ENDANGERED", "EXTINCT" }, example = "ALIVE")
    String status;

    public DinosaurResponse(Long id, String name, String species, LocalDateTime discoveryDate,
            LocalDateTime extinctionDate, String status) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = status;
    }

    public DinosaurResponse() {
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecies() {
        return species;
    }

    public LocalDateTime getDiscoveryDate() {
        return discoveryDate;
    }

    public LocalDateTime getExtinctionDate() {
        return extinctionDate;
    }

    public String getStatus() {
        return status;
    }

}