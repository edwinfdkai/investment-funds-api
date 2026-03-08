package com.test.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class Client {

    private String id;
    private String name;
    private String email;
    private String phone;
    private Long balance;
    private String notificationPreference;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }
}
