package com.dinosaur.infrastructure.in.scheduler;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.dinosaur.application.port.in.UpdateScheduledDinosaurStatusesUseCase;

@Component
public class DinosaurStatusScheduler {

    private final UpdateScheduledDinosaurStatusesUseCase updateScheduledDinosaurStatusesUseCase;

    public DinosaurStatusScheduler(UpdateScheduledDinosaurStatusesUseCase updateScheduledDinosaurStatusesUseCase) {
        this.updateScheduledDinosaurStatusesUseCase = updateScheduledDinosaurStatusesUseCase;
    }

    @Scheduled(cron = "${scheduler.cron-expression}")
    public void updateDinosaurStatus() {
        updateScheduledDinosaurStatusesUseCase.execute();
    }
}
