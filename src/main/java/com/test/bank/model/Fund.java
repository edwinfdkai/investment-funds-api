package com.test.bank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fund {

    private Long id;
    private String name;
    private Long minAmount;
    private String categoria;
}
