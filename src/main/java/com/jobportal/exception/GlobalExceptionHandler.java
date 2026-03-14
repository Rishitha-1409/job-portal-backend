package com.jobportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(err -> {
            String field = ((FieldError) err).getField();
            String message = err.getDefaultMessage();
            fieldErrors.put(field, message);
        });

        ErrorResponse body = new ErrorResponse(
                400,
                "Validation Failed",
                "One or more fields are invalid",
                fieldErrors,
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        String msg = String.format(
                "Invalid value '%s' for parameter '%s'",
                ex.getValue(),
                ex.getName()
        );

        return ResponseEntity.badRequest()
                .body(ErrorResponse.simple(400, "Bad Request", msg));
    }

    // ================= BUSINESS =================

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.simple(404, "Not Found", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
                .body(ErrorResponse.simple(400, "Bad Request", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalState(IllegalStateException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.simple(409, "Conflict", ex.getMessage()));
    }

    // ================= SECURITY =================

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.simple(401, "Unauthorized", "Invalid email or password"));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.simple(403, "Forbidden", ex.getMessage()));
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleDisabled(DisabledException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.simple(403, "Account Disabled", "Your account has been disabled"));
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ErrorResponse> handleLocked(LockedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.simple(403, "Account Locked", "Your account has been locked"));
    }

    // ================= GLOBAL =================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {

        ex.printStackTrace();

        return ResponseEntity.internalServerError()
                .body(ErrorResponse.simple(
                        500,
                        "Internal Server Error",
                        "An unexpected error occurred"
                ));
    }

    // ================= ERROR RESPONSE DTO =================

    public static class ErrorResponse {

        private int status;
        private String error;
        private String message;
        private Map<String, String> fieldErrors;
        private LocalDateTime timestamp;

        public ErrorResponse() {}

        public ErrorResponse(int status, String error, String message,
                             Map<String, String> fieldErrors,
                             LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.fieldErrors = fieldErrors;
            this.timestamp = timestamp;
        }

        public static ErrorResponse simple(int status, String error, String message) {
            return new ErrorResponse(
                    status,
                    error,
                    message,
                    null,
                    LocalDateTime.now()
            );
        }

        // Getters and Setters

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Map<String, String> getFieldErrors() {
            return fieldErrors;
        }

        public void setFieldErrors(Map<String, String> fieldErrors) {
            this.fieldErrors = fieldErrors;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
    }
}