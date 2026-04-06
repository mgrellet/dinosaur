package com.dinosaur.infrastructure.in.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.port.in.DeleteDinosaurUseCase;
import com.dinosaur.application.port.in.GetAllDinosaursUseCase;
import com.dinosaur.application.port.in.GetDinosaurByIdUseCase;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.application.port.in.UpdateScheduledDinosaurStatusesUseCase;
import com.dinosaur.application.service.CreateDinosaurUseCaseImpl;
import com.dinosaur.application.service.DeleteDinosaurUseCaseImpl;
import com.dinosaur.application.service.GetAllDinosaursUseCaseImpl;
import com.dinosaur.application.service.GetDinosaurByIdUseCaseImpl;
import com.dinosaur.application.service.UpdateDinosaurUseCaseImpl;
import com.dinosaur.application.service.UpdateScheduledDinosaurStatusesUseCaseImpl;
import com.dinosaur.domain.port.out.DinosaurMessagingPort;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreateDinosaurUseCase createDinosaurUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new CreateDinosaurUseCaseImpl(dinosaurPersistencePort);
    }

    @Bean
    public GetAllDinosaursUseCase getAllDinosaursUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new GetAllDinosaursUseCaseImpl(dinosaurPersistencePort);
    }

    @Bean
    public GetDinosaurByIdUseCase getDinosaurByIdUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new GetDinosaurByIdUseCaseImpl(dinosaurPersistencePort);
    }

    @Bean
    public UpdateDinosaurUseCase updateDinosaurUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new UpdateDinosaurUseCaseImpl(dinosaurPersistencePort);
    }

    @Bean
    public DeleteDinosaurUseCase deleteDinosaurUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new DeleteDinosaurUseCaseImpl(dinosaurPersistencePort);
    }

    @Bean
    public UpdateScheduledDinosaurStatusesUseCase updateScheduledDinosaurStatusesUseCase(
            DinosaurPersistencePort dinosaurPersistencePort, DinosaurMessagingPort dinosaurMessagingPort) {
        return new UpdateScheduledDinosaurStatusesUseCaseImpl(dinosaurPersistencePort, dinosaurMessagingPort);
    }
}
