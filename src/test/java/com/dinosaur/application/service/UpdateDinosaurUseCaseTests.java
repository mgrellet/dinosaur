package com.dinosaur.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.domain.exception.DinosaurAlreadyExistsException;
import com.dinosaur.domain.exception.DinosaurInvalidDateException;
import com.dinosaur.domain.exception.DinosaurNotFoundException;
import com.dinosaur.domain.exception.InvalidDinosaurException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@ExtendWith(MockitoExtension.class)
public class UpdateDinosaurUseCaseTests {

    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    private UpdateDinosaurUseCase updateDinosaurUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        updateDinosaurUseCase = new UpdateDinosaurUseCaseImpl(dinosaurPersistencePort);
        now = LocalDateTime.now();
    }

    @Test
    void testUpdateDinosaur_ShouldUpdateDinosaur() {
        Dinosaur dinosaur = new Dinosaur("Triceratops", "Herbivore", now, now.plusDays(10), DinosaurStatus.ENDANGERED);
        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.of(dinosaur));
        when(dinosaurPersistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.ENDANGERED.name());
        DinosaurResult result = updateDinosaurUseCase.execute(1L, command);
        assertEquals(DinosaurStatus.ENDANGERED.name(), result.status());
        verify(dinosaurPersistencePort).save(any(Dinosaur.class));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfDinosaurNotFound() {
        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.empty());
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.ENDANGERED.name());
        assertThrows(DinosaurNotFoundException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfNameExists() {
        when(dinosaurPersistencePort.existsByNameAndIdNot("Triceratops", 1L)).thenReturn(true);
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.ENDANGERED.name());
        assertThrows(DinosaurAlreadyExistsException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfDiscoveryDateIsAfterExtinctionDate() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now.plusDays(10), now,
                DinosaurStatus.ENDANGERED.name());
        assertThrows(DinosaurInvalidDateException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfStatusIsNull() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10), null);
        assertThrows(InvalidDinosaurException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfStatusIsBlank() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10), "   ");
        assertThrows(InvalidDinosaurException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfStatusIsInvalid() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10), "UNKNOWN");
        assertThrows(InvalidDinosaurException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfDinosaurIsExtinct() {
        Dinosaur extinctDinosaur = new Dinosaur("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.EXTINCT);
        when(dinosaurPersistencePort.findById(1L)).thenReturn(Optional.of(extinctDinosaur));
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.ALIVE.name());
        assertThrows(InvalidDinosaurException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }

    @Test
    void testUpdateDinosaur_ShouldThrowExceptionIfDiscoveryDateEqualsExtinctionDate() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now,
                DinosaurStatus.ENDANGERED.name());
        assertThrows(DinosaurInvalidDateException.class, () -> updateDinosaurUseCase.execute(1L, command));
    }
}
