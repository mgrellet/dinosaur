package com.dinosaur.domain.exception;

public class InvalidDinosaurException extends RuntimeException {
    public InvalidDinosaurException(String message) {
        super(message);
    }    
}
