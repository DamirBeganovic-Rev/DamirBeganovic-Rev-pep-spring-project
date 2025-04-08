package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST API errors.
 * 
 * This class uses Spring's @RestControllerAdvice to handle specific exceptions thrown
 * accross the application and convert them into useful HTTP responses.
 */
@RestControllerAdvice
public class ExceptionAndErrorController {

    /**
     * Handles validation errors during user registration.
     *
     * Maps different `IllegalArgumentException` messages to appropriate HTTP status codes:
     * - 409 Conflict if the username already exists.
     * - 400 Bad Request if the username is blank or the password is too short.
     *
     * @param ex the thrown IllegalArgumentException during registration.
     * @return a ResponseEntity with the appropriate status code and the exception message in the response body.
     */
    @ExceptionHandler (IllegalArgumentException.class)
    public ResponseEntity<Object> handleRegistrationErrors(IllegalArgumentException ex) {
        String exMsg = ex.getMessage();

        // --- Account registration errors ---

        // Return status code 409 (Conflict) if the username already exists in the database
        if (exMsg.contains("Username already exists")) {
            return ResponseEntity.status(409).body(exMsg); 
        }
        // Return status code 400 (Bad Request) if the username is blank or the password is too short
        else if (exMsg.contains("cannot be blank") || exMsg.contains("at least 4 characters")) {
            return ResponseEntity.status(400).body(exMsg);
        }

        // --- Message related errors ---

        // Return status code 400 (Bad Request) if the message's postedBy ID is not in the database
        else if (exMsg.contains("The account does not exist")){
            return ResponseEntity.status(400).body(exMsg);
        }
        // Return status code 400 (Bad Request) if the message ID is not in the database
        else if (exMsg.contains("The message does not exist")){
            return ResponseEntity.status(400).body(exMsg);
        }
        // Return status code 400 (Bad Request) if the message's text is blank or too long
        else if (exMsg.contains("Message cannot be blank or over 255 characters")){
            return ResponseEntity.status(400).body(exMsg);
        }

        else {
            // Default to status code 400 (Bad Request) for any other invalid inputs
            return ResponseEntity.status(400).body("Invalid request. Please check your input.");
        }
    }

    /**
     * Handles login errors when user credentials are invalid.
     *
     * Catches `InvalidLoginException` and returns a 401 Unauthorized response.
     *
     * @param ex the thrown InvalidLoginException.
     * @return a ResponseEntity with HTTP 401 status and the exception message in the response body.
     */
    @ExceptionHandler (InvalidLoginException.class)
    public ResponseEntity<Object> handleLoginErrors(InvalidLoginException ex){
        return ResponseEntity.status(401).body(ex.getMessage());
    }

    /**
     * Handles cases where a requested resource (such as a message or account) is not found.
     * 
     * Catches ResourceNotFoundException instances thrown by the service layer
     * and returns an HTTP 404 Not Found response with a descriptive error message.
     * 
     * @param ex the thrown ResourceNotFoundException.
     * @return a ResponseEntity with HTTP 404 status and the exception message in the response body.
     */
    @ExceptionHandler (ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundErrors(ResourceNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
