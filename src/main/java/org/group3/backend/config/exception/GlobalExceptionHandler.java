/*
 * @Author : Thant Htoo Aung
 * @Date : 1/13/2024
 * @Time : 09:10 PM
 */
package org.group3.backend.config.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for centralized exception management across the application.
 * Provides tailored responses for various exception types to ensure consistent and user-friendly error messages.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles IllegalArgumentExceptions, typically thrown when method arguments are invalid or inappropriate.
     *
     * @param ex      the IllegalArgumentException encountered.
     * @param request the current web request.
     * @return a ResponseEntity object containing the error details and the BAD_REQUEST status.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, "Invalid argument provided.", ex);
    }

    /**
     * Handles ConstraintViolationException, typically occurring during input validation.
     *
     * @param ex      the ConstraintViolationException encountered.
     * @param request the current web request.
     * @return a ResponseEntity object containing the validation errors and the UNPROCESSABLE_ENTITY status.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed.", ex);
    }

    /**
     * Handles EntityNotFoundException, thrown when an entity cannot be located in the database.
     *
     * @param ex      the EntityNotFoundException encountered.
     * @param request the current web request.
     * @return a ResponseEntity object containing the error details and the NOT_FOUND status.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, "Entity not found.", ex);
    }

    /**
     * Handles DuplicateEntityException, thrown when attempting to create an entity that already exists.
     *
     * @param ex      the DuplicateEntityException encountered.
     * @param request the current web request.
     * @return a ResponseEntity object containing the error details and the CONFLICT status.
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Object> handleDuplicateEntityException(DuplicateEntityException ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.CONFLICT, "Duplicate entity detected.", ex);
    }

    /**
     * Handles all uncaught exceptions, providing a generic error response.
     *
     * @param ex      the Exception encountered.
     * @param request the current web request.
     * @return a ResponseEntity object containing the error details and the INTERNAL_SERVER_ERROR status.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.", ex);
    }

    /**
     * Utility method to construct a consistent error response structure.
     *
     * @param status  the HTTP status to be returned.
     * @param message a brief description of the error.
     * @param ex      the exception encountered.
     * @return a ResponseEntity containing the error details.
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message, Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("details", ex.getMessage());
        return new ResponseEntity<>(body, status);
    }
}