package com.test.bank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    private String id;
    private String clientId;
    private String fundId;
    private String type;
    private Long amount;
    private LocalDate date;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
