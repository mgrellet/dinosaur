package com.dinosaur.domain.exception;

public class DinosaurAlreadyExistsException extends RuntimeException {
    public DinosaurAlreadyExistsException(String message) {
        super(message);
    }
}
