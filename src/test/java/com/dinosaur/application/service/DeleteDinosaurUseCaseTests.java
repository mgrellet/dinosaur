package com.dinosaur.application.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;

import com.dinosaur.application.port.in.DeleteDinosaurUseCase;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;

@ExtendWith(MockitoExtension.class)
public class DeleteDinosaurUseCaseTests {

    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    private DeleteDinosaurUseCase deleteDinosaurUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        deleteDinosaurUseCase = new DeleteDinosaurUseCaseImpl(dinosaurPersistencePort);
        now = LocalDateTime.now();
    }

    @Test
    void testDeleteDinosaur_ShouldDeleteDinosaur() {
        Dinosaur dinosaur = new Dinosaur("Triceratops", "Herbivore", now, now.plusDays(10), DinosaurStatus.ALIVE);
        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.of(dinosaur));
        deleteDinosaurUseCase.execute(1L);
        verify(dinosaurPersistencePort).delete(dinosaur);
    }
}
