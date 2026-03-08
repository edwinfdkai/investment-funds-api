package com.test.bank.dto;

import lombok.Data;

@Data
public class CancelRequest {

    private Long clientId;
    private Long fundId;
}
