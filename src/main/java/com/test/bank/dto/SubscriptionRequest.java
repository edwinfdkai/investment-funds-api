package com.test.bank.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {

    private String clientId;
    private String fundId;
}