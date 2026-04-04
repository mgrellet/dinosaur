package com.dinosaur.infrastructure.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.dto.DinosaurResponse;
import com.dinosaur.infrastructure.in.web.mapper.InfrastructureMapper;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/dinosaur")
public class DinosaurController {

    private final CreateDinosaurUseCase createDinosaurUseCase;

    public DinosaurController(CreateDinosaurUseCase createDinosaurUseCase) {
        this.createDinosaurUseCase = createDinosaurUseCase;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DinosaurResponse> create(@Valid @RequestBody DinosaurRequest request) {

        DinosaurResult result = createDinosaurUseCase.execute(InfrastructureMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(InfrastructureMapper.toResponse(result));
    }
}
