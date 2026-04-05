package com.dinosaur.domain.exception;

public class DinosaurInvalidDateException extends RuntimeException {
    public DinosaurInvalidDateException(String message) {
        super(message);
    }
}
