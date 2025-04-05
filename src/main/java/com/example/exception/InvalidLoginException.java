package com.example.exception;

/**
 * Custom exception thrown when a user's login attempt fails due to invalid credentials.
 * 
 * This exception is used to indicate that the provided username and/or password did not
 * match any existing account in the database.
 * 
 * It is handled globally by the ExceptionAndErrorController and will return a 401 Unauthorized
 * HTTP response.
 */
public class InvalidLoginException extends RuntimeException {
    /**
     * Constructor for a new InvalidLoginException with the specified detail message.
     * 
     * @param message The detail message which describes the reason for the exception
     */
    public InvalidLoginException(String message) {
        super(message);
    }
}