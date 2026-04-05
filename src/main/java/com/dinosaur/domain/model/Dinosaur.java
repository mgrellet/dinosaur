package com.dinosaur.domain.model;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;

public class Dinosaur {
    private Long id;
    private String name;
    private String species;
    private LocalDateTime discoveryDate;
    private LocalDateTime extinctionDate;
    private DinosaurStatus status;

    public Dinosaur(Long id, String name, String species, LocalDateTime discoveryDate, LocalDateTime extinctionDate,
            DinosaurStatus status) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = isNull(status) ? DinosaurStatus.ALIVE : status;
    }

    public Dinosaur(String name, String species, LocalDateTime discoveryDate, LocalDateTime extinctionDate,
            DinosaurStatus status) {
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = isNull(status) ? DinosaurStatus.ALIVE : status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public LocalDateTime getDiscoveryDate() {
        return discoveryDate;
    }

    public void setDiscoveryDate(LocalDateTime discoveryDate) {
        this.discoveryDate = discoveryDate;
    }

    public LocalDateTime getExtinctionDate() {
        return extinctionDate;
    }

    public void setExtinctionDate(LocalDateTime extinctionDate) {
        this.extinctionDate = extinctionDate;
    }

    public DinosaurStatus getStatus() {
        return status;
    }

    public void setStatus(DinosaurStatus status) {
        this.status = isNull(status) ? DinosaurStatus.ALIVE : status;
    }

}
