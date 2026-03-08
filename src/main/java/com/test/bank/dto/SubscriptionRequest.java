package com.test.bank.dto;

import lombok.Data;

@Data
public class SubscriptionRequest {

    private Long clientId;
    private Long fundId;
}