package com.test.bank.dto;

import lombok.Data;

@Data
public class CancelRequest {

    private String clientId;
    private String fundId;
}
