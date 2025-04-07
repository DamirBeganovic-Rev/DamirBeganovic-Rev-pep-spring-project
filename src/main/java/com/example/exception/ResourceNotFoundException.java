package com.example.exception;

/**
 * Custom exception thrown to indicate that a requested resource was not 
 * found in the system.
 *
 * This exception is used in service-layer methods where a
 * database entity (such as a Message or Account) is expected to exist,
 * but cannot be found based on the provided id
 * 
 * It is handled globally by the ExceptionAndErrorController and will return a
 * 404 Not Found HTTP response.
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructor for a new ResourceNotFoundException with the specified detail message.
     *
     * @param message A detailed message explaining what resource was not found.
     */
    public ResourceNotFoundException(String message){
        super(message);
    }
}
