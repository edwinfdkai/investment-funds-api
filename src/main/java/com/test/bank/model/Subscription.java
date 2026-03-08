package com.test.bank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {

    private Long clientId;
    private Long fundId;
    private Long amount;
    private String status;
    private LocalDate dateOpened;
}