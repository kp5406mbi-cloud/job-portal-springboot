package com.jobportal.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(404).body(
                Map.of(
                        "status", 404,
                        "error", ex.getMessage()
                )
        );
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserExists(UserAlreadyExistsException ex) {

        return ResponseEntity.status(409).body(
                Map.of(
                        "status", 409,
                        "error", ex.getMessage()
                )
        );
    }
}