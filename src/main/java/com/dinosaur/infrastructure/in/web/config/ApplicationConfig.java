package com.dinosaur.infrastructure.in.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.service.CreateDinosaurUseCaseImpl;
import com.dinosaur.domain.port.out.DinosaurPersistencePort;

@Configuration
public class ApplicationConfig {
    
    @Bean
    public CreateDinosaurUseCase createDinosaurUseCase(DinosaurPersistencePort dinosaurPersistencePort) {
        return new CreateDinosaurUseCaseImpl(dinosaurPersistencePort);
    }
}
