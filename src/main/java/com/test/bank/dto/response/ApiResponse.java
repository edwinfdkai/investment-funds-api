package com.test.bank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

/**
 * Standard envelope for all API responses (success and error payloads use the same shape).
 */
@Value
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    boolean success;
    String message;
    T data;
    Instant timestamp;
    /** Present on error responses (e.g. VALIDATION_ERROR, BUSINESS_ERROR). */
    String code;

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(Instant.now())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, T data, String code) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(data)
                .code(code)
                .timestamp(Instant.now())
                .build();
    }
}
