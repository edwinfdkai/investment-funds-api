package com.test.bank.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.LocalDate;

@DynamoDbBean
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    private String clientId;
    private String fundId;
    private Long amount;
    private String status;
    private LocalDate dateOpened;

    @DynamoDbPartitionKey
    public String getClientId() {
        return clientId;
    }

    @DynamoDbSortKey
    public String getFundId() {
        return fundId;
    }
}