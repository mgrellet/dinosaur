package com.dinosaur.domain.exception;

public class DinosaurNotFoundException extends RuntimeException {
    public DinosaurNotFoundException(String message) {
        super(message);
    }
}
