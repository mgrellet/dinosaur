package com.dinosaur.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinosaur.application.dto.DinosaurCommand;
import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.domain.exception.DinosaurAlreadyExistsException;
import com.dinosaur.domain.exception.DinosaurInvalidDateException;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CreateDinosaurUseCaseTests {

    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    private CreateDinosaurUseCase createDinosaurUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        createDinosaurUseCase = new CreateDinosaurUseCaseImpl(dinosaurPersistencePort);
        now = LocalDateTime.now();
    }

    @Test
    void testCreateDinosaur_ShouldSetAliveIfNoStatus() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10), null);
        when(dinosaurPersistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DinosaurResult result = createDinosaurUseCase.execute(command);

        assertEquals("ALIVE", result.status());
        verify(dinosaurPersistencePort).save(any(Dinosaur.class));
    }

    @Test
    void testCreateDinosaur_ShouldCreateDinosaurWithStatus() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10),
                DinosaurStatus.ENDANGERED.name());
        when(dinosaurPersistencePort.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        DinosaurResult result = createDinosaurUseCase.execute(command);

        assertEquals(DinosaurStatus.ENDANGERED.name(), result.status());
        verify(dinosaurPersistencePort).save(any(Dinosaur.class));
    }

    @Test
    void testCreateDinosaur_ShouldThrowExceptionIfNameExists() {
        when(dinosaurPersistencePort.existsByName("Triceratops")).thenReturn(true);
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now, now.plusDays(10), null);
        assertThrows(DinosaurAlreadyExistsException.class, () -> createDinosaurUseCase.execute(command));
    }

    @Test
    void testCreateDinosaur_ShouldThrowExceptionIfDiscoveryDateIsAfterExtinctionDate() {
        DinosaurCommand command = new DinosaurCommand("Triceratops", "Herbivore", now.plusDays(10), now, null);
        assertThrows(DinosaurInvalidDateException.class, () -> createDinosaurUseCase.execute(command));
    }

}
