package com.test.bank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelRequest {

    @NotBlank
    private String clientId;

    @NotBlank
    private String fundId;
}
