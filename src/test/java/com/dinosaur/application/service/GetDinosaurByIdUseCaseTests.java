package com.dinosaur.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.GetDinosaurByIdUseCase;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@ExtendWith(MockitoExtension.class)
public class GetDinosaurByIdUseCaseTests {
    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    private GetDinosaurByIdUseCase getDinosaurByIdUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        getDinosaurByIdUseCase = new GetDinosaurByIdUseCaseImpl(dinosaurPersistencePort);
        now = LocalDateTime.now();
    }

    @Test
    void testGetDinosaurById_ShouldReturnDinosaur() {
        Dinosaur dinosaur = new Dinosaur("Triceratops", "Herbivore", now, now.plusDays(10), DinosaurStatus.ALIVE);

        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.of(dinosaur));

        DinosaurResult result = getDinosaurByIdUseCase.execute(1L);

        assertEquals(dinosaur.getName(), result.name());
        assertEquals(dinosaur.getSpecies(), result.species());
        assertEquals(dinosaur.getDiscoveryDate(), result.discoveryDate());
        assertEquals(dinosaur.getExtinctionDate(), result.extinctionDate());
        assertEquals(dinosaur.getStatus().name(), result.status());
    }

    @Test
    void testGetDinosaurById_ShouldThrowExceptionIfDinosaurNotFound() {
        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.empty());
        assertThrows(DinosaurNotFoundException.class, () -> getDinosaurByIdUseCase.execute(1L));
    }
}
