package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiError", description = "Cuerpo de error devuelto por la API (mismo esquema para 4xx/5xx; el código y el texto dependen del caso)")
public record ApiError(
        @Schema(description = "Momento del error en el servidor (ISO-8601)")
        LocalDateTime timestamp,
        @Schema(description = "Código HTTP de la respuesta (p. ej. 400, 404, 500)")
        int statusCode,
        @Schema(description = "Nombre del estado HTTP (p. ej. BAD_REQUEST, NOT_FOUND)")
        String message,
        @Schema(description = "Detalle del error o mensajes de validación")
        String errorMessage,
        @Schema(description = "Ruta HTTP solicitada")
        String path) {
}

