package com.dinosaur.infrastructure.in.web.dto;

import java.time.LocalDateTime;

public record ApiError(LocalDateTime timestamp, int statusCode, String message, String errorMessage, String path) {
} 
