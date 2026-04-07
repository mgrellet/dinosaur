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
import com.dinosaur.infrastructure.in.web.dto.ApiError;
import com.dinosaur.infrastructure.in.web.dto.DinosaurRequest;
import com.dinosaur.infrastructure.in.web.dto.DinosaurResponse;
import com.dinosaur.infrastructure.in.web.mapper.InfrastructureMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/dinosaur")
@Tag(name = "Dinosaurios", description = "Operaciones AMB sobre dinosaurios")
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
    @Operation(summary = "Crear dinosaurio", description = "Registra un nuevo dinosaurio. Las fechas deben cumplir las reglas de dominio.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Dinosaurio creado",
                    content = @Content(schema = @Schema(implementation = DinosaurResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validación o reglas de negocio",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<DinosaurResponse> create(@Valid @RequestBody DinosaurRequest request) {

        DinosaurResult result = createDinosaurUseCase.execute(InfrastructureMapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(InfrastructureMapper.toResponse(result));
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Listar dinosaurios", description = "Devuelve todos los dinosaurios registrados.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = DinosaurResponse.class))))
    public ResponseEntity<List<DinosaurResponse>> getAll() {
        List<DinosaurResult> results = getAllDinosaursUseCase.execute();
        return ResponseEntity.ok(results.stream().map(InfrastructureMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Obtener dinosaurio por id", description = "Consulta un dinosaurio por su identificador numérico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Dinosaurio encontrado",
                    content = @Content(schema = @Schema(implementation = DinosaurResponse.class))),
            @ApiResponse(responseCode = "404", description = "No existe un dinosaurio con ese id",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<DinosaurResponse> getById(
            @Parameter(description = "Identificador del dinosaurio", example = "1") @PathVariable Long id) {
        DinosaurResult result = getDinosaurByIdUseCase.execute(id);
        return ResponseEntity.ok(InfrastructureMapper.toResponse(result));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Actualizar dinosaurio", description = "Modifica los datos de un dinosaurio existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Actualización correcta",
                    content = @Content(schema = @Schema(implementation = DinosaurResponse.class))),
            @ApiResponse(responseCode = "400", description = "Cuerpo inválido o reglas de negocio",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "No existe un dinosaurio con ese id",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<DinosaurResponse> update(
            @Parameter(description = "Identificador del dinosaurio", example = "1") @PathVariable Long id,
            @Valid @RequestBody DinosaurRequest request) {
        DinosaurResult result = updateDinosaurUseCase.execute(id, InfrastructureMapper.toCommand(request));
        return ResponseEntity.ok(InfrastructureMapper.toResponse(result));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar dinosaurio", description = "Elimina un dinosaurio por id. Respuesta sin cuerpo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un dinosaurio con ese id",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "Identificador del dinosaurio", example = "1") @PathVariable Long id) {
        deleteDinosaurUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
