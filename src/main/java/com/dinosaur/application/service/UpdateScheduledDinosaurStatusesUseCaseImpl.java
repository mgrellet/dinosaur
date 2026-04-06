package com.dinosaur.application.service;

import java.util.List;
import java.time.LocalDateTime;

import com.dinosaur.application.port.in.UpdateScheduledDinosaurStatusesUseCase;
import com.dinosaur.domain.model.Dinosaur;
import com.dinosaur.domain.model.DinosaurStatus;
import com.dinosaur.domain.port.out.DinosaurMessagingPort;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

public class UpdateScheduledDinosaurStatusesUseCaseImpl implements UpdateScheduledDinosaurStatusesUseCase {

    private final DinosaurPersistencePort dinosaurPersistencePort;
    private final DinosaurMessagingPort dinosaurMessagingPort;

    public UpdateScheduledDinosaurStatusesUseCaseImpl(DinosaurPersistencePort dinosaurPersistencePort,
            DinosaurMessagingPort dinosaurMessagingPort) {
        this.dinosaurPersistencePort = dinosaurPersistencePort;
        this.dinosaurMessagingPort = dinosaurMessagingPort;
    }

    @Override
    public void execute() {

        List<Dinosaur> dinosaurs = dinosaurPersistencePort.findAliveOrEndangered();
        LocalDateTime now = LocalDateTime.now();

        dinosaurs.forEach(dinosaur -> {
            LocalDateTime extinction = dinosaur.getExtinctionDate();
            boolean hasReachedExtinction = now.isAfter(extinction) || now.isEqual(extinction);
            boolean hasEnteredEndangeredWindow = now.isAfter(extinction.minusHours(24))
                    || now.isEqual(extinction.minusHours(24));

            DinosaurStatus previousStatus = dinosaur.getStatus();
        
            if (hasReachedExtinction) {
                dinosaur.setStatus(DinosaurStatus.EXTINCT);
            } else if (dinosaur.getStatus() == DinosaurStatus.ALIVE && hasEnteredEndangeredWindow) {
                dinosaur.setStatus(DinosaurStatus.ENDANGERED);
            }
            boolean statusChanged = previousStatus != dinosaur.getStatus();

            if (statusChanged) {
                dinosaurPersistencePort.save(dinosaur);
                dinosaurMessagingPort.sendStatusUpdate(dinosaur.getId(), dinosaur.getStatus().name(), now);
            }
        });

    }
}
