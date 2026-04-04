package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

public class DinosaurResponse {
    Long id;
    String name;
    String species;
    LocalDateTime discoveryDate;
    LocalDateTime extinctionDate;
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