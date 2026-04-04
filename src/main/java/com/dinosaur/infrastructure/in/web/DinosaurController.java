package com.dinosaur.infrastructure.in.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.dinosaur.application.dto.DinosaurResult;
import com.dinosaur.application.port.in.CreateDinosaurUseCase;
import com.dinosaur.application.port.in.DeleteDinosaurUseCase;
import com.dinosaur.application.port.in.GetAllDinosaursUseCase;
import com.dinosaur.application.port.in.GetDinosaurByIdUseCase;
import com.dinosaur.application.port.in.UpdateDinosaurUseCase;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.dto.DinosaurResponse;
import com.dinosaur.infrastructure.in.web.mapper.InfrastructureMapper;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/dinosaur")
public class DinosaurController {

    private final CreateDinosaurUseCase createDinosaurUseCase;
    private final GetAllDinosaursUseCase getAllDinosaursUseCase;
    private final GetDinosaurByIdUseCase getDinosaurByIdUseCase;
    private final UpdateDinosaurUseCase updateDinosaurUseCase;
    private final DeleteDinosaurUseCase deleteDinosaurUseCase;
    
    public DinosaurController(CreateDinosaurUseCase createDinosaurUseCase,
            GetAllDinosaursUseCase getAllDinosaursUseCase,
            GetDinosaurByIdUseCase getDinosaurByIdUseCase,
            UpdateDinosaurUseCase updateDinosaurUseCase,
            DeleteDinosaurUseCase deleteDinosaurUseCase) {
        this.createDinosaurUseCase = createDinosaurUseCase;
        this.getAllDinosaursUseCase = getAllDinosaursUseCase;
        this.getDinosaurByIdUseCase = getDinosaurByIdUseCase;
        this.updateDinosaurUseCase = updateDinosaurUseCase;
        this.deleteDinosaurUseCase = deleteDinosaurUseCase;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DinosaurResponse> create(@Valid @RequestBody DinosaurRequest request) {

        DinosaurResult result = createDinosaurUseCase.execute(InfrastructureMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(InfrastructureMapper.toResponse(result));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<DinosaurResponse>> getAll() {
        List<DinosaurResult> results = getAllDinosaursUseCase.execute();
        return ResponseEntity.ok(results.stream().map(InfrastructureMapper::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DinosaurResponse> getById(@PathVariable Long id) {
        DinosaurResult result = getDinosaurByIdUseCase.execute(id);
        return ResponseEntity.ok(InfrastructureMapper.toResponse(result));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<DinosaurResponse> update(@PathVariable Long id, @Valid @RequestBody DinosaurRequest request) {
        DinosaurResult result = updateDinosaurUseCase.execute(id, InfrastructureMapper.toCommand(request));
        return ResponseEntity.ok(InfrastructureMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteDinosaurUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
