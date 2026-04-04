package com.dinosaur.infrastructure.out.persistance;

import java.time.LocalDateTime;

import com.dinosaur.domain.model.DinosaurStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "dinosaurs")
public class DinosaurJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String species;

    @Column(nullable = false)
    private LocalDateTime discoveryDate;

    @Column(nullable = false)
    private LocalDateTime extinctionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DinosaurStatus status;

    public DinosaurJpaEntity(String name, String species, LocalDateTime discoveryDate, LocalDateTime extinctionDate, DinosaurStatus status) {
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = status;
    }

    public DinosaurJpaEntity(Long id, String name, String species, LocalDateTime discoveryDate, LocalDateTime extinctionDate, DinosaurStatus status) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.discoveryDate = discoveryDate;
        this.extinctionDate = extinctionDate;
        this.status = status;
    }
    public DinosaurJpaEntity() {
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
        this.status = status;
    }

    
}
