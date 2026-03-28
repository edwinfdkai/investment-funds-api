package com.test.bank.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateSubscriptionRequest {

    @NotBlank(message = "fundId must not be blank")
    private String fundId;
}
