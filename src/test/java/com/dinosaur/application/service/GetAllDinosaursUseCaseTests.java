package com.dinosaur.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.GetAllDinosaursUseCase;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@ExtendWith(MockitoExtension.class)
public class GetAllDinosaursUseCaseTests {

    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    private GetAllDinosaursUseCase getAllDinosaursUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        getAllDinosaursUseCase = new GetAllDinosaursUseCaseImpl(dinosaurPersistencePort);
        now = LocalDateTime.now();
    }

    @Test
    void testGetAllDinosaurs_ShouldReturnAllDinosaurs() {

        List<Dinosaur> dinosaurs = Arrays.asList(
                new Dinosaur("Triceratops", "Herbivore", now, now.plusDays(10), DinosaurStatus.ALIVE),
                new Dinosaur("Velociraptor", "Carnivore", now, now.plusDays(10), DinosaurStatus.ALIVE));
        when(dinosaurPersistencePort.findAll()).thenReturn(dinosaurs);

        List<DinosaurResult> result = getAllDinosaursUseCase.execute();
        assertEquals(dinosaurs.size(), result.size());
        assertEquals(dinosaurs.get(0).getName(), result.get(0).name());
        assertEquals(dinosaurs.get(0).getSpecies(), result.get(0).species());
        assertEquals(dinosaurs.get(0).getDiscoveryDate(), result.get(0).discoveryDate());
        assertEquals(dinosaurs.get(0).getExtinctionDate(), result.get(0).extinctionDate());
        assertEquals(dinosaurs.get(0).getStatus().name(), result.get(0).status());
        assertEquals(dinosaurs.get(1).getName(), result.get(1).name());
        assertEquals(dinosaurs.get(1).getSpecies(), result.get(1).species());
        assertEquals(dinosaurs.get(1).getDiscoveryDate(), result.get(1).discoveryDate());
        assertEquals(dinosaurs.get(1).getExtinctionDate(), result.get(1).extinctionDate());
        assertEquals(dinosaurs.get(1).getStatus().name(), result.get(1).status());
    }
}
