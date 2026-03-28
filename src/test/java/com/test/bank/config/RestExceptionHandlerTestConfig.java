package com.test.bank.config;

import com.test.bank.exception.GlobalExceptionHandler;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * Registers API exception handling in {@code @WebMvcTest} slices without importing nested types
 * from test classes (which can break context loading).
 */
@TestConfiguration(proxyBeanMethods = false)
public class RestExceptionHandlerTestConfig {

    @Bean
    GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
