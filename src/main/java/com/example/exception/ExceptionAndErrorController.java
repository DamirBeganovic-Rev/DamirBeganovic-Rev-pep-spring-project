package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAndErrorController {

    @ExceptionHandler
    public ResponseEntity<Object> handleRegistrationErrors(IllegalArgumentException ex) {
        // (Conflict) Username already exists in the database
        if (ex.getMessage().contains("Username already exists")) {
            return ResponseEntity.status(409).body(ex.getMessage()); 
        }
        // (Client Error) Bad request due to invalid username and password entered
        else if (ex.getMessage().contains("cannot be blank") || ex.getMessage().contains("at least 4 characters")) {
            return ResponseEntity.status(400).body(ex.getMessage());
        }
        else {
            return ResponseEntity.status(400).body("Invalid account deltails. Please try again");
        }
    }
}
