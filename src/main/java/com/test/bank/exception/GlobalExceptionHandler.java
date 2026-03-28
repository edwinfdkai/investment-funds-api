package com.test.bank.exception;

import com.test.bank.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String GENERIC_SERVER_ERROR =
            "An unexpected error occurred. Please try again later.";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ex.getMessage(), null, "BUSINESS_ERROR"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "invalid",
                        (a, b) -> a));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", fieldErrors, "VALIDATION_ERROR"));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
            ConstraintViolationException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath().toString();
            String field = path.contains(".") ? path.substring(path.lastIndexOf('.') + 1) : path;
            errors.put(field, v.getMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed", errors, "VALIDATION_ERROR"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(GENERIC_SERVER_ERROR, null, "INTERNAL_ERROR"));
    }
}
