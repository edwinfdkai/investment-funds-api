package com.test.bank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String id;
    private Long clientId;
    private Long fundId;
    private String type;
    private Long amount;
    private LocalDate date;
}
