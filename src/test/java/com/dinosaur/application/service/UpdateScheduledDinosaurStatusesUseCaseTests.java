package com.dinosaur.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinosaur.application.port.in.UpdateScheduledDinosaurStatusesUseCase;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurMessagingPort;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@ExtendWith(MockitoExtension.class)
public class UpdateScheduledDinosaurStatusesUseCaseTests {

    @Mock
    private DinosaurPersistencePort dinosaurPersistencePort;

    @Mock
    private DinosaurMessagingPort dinosaurMessagingPort;

    private UpdateScheduledDinosaurStatusesUseCase updateScheduledDinosaurStatusesUseCase;

    LocalDateTime now;

    @BeforeEach
    void setUp() {
        updateScheduledDinosaurStatusesUseCase = new UpdateScheduledDinosaurStatusesUseCaseImpl(dinosaurPersistencePort, dinosaurMessagingPort);
        now = LocalDateTime.now();
    }

    @Test
    void testUpdateScheduledStatuses_ShouldTriggerChanges() {
        Dinosaur dinosaur = new Dinosaur(1L, "T-Rex", "Carnivore", now.minusDays(10), now, DinosaurStatus.ALIVE);
        when(dinosaurPersistencePort.findAliveOrEndangered()).thenReturn(List.of(dinosaur));
        when(dinosaurPersistencePort.save(any())).thenReturn(dinosaur);

        updateScheduledDinosaurStatusesUseCase.execute();

        verify(dinosaurPersistencePort).save(dinosaur);
        verify(dinosaurMessagingPort).sendStatusUpdate(eq(dinosaur.getId()), any(), any());
        assertEquals(DinosaurStatus.EXTINCT, dinosaur.getStatus());
    }


}
