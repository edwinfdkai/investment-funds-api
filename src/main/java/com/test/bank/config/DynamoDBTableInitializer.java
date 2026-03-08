package com.test.bank.config;

import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import com.test.bank.model.Transaction;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Component
@RequiredArgsConstructor
public class DynamoDBTableInitializer {

    private final DynamoDbClient dynamoDbClient;

    @PostConstruct
    public void init() {

        DynamoDbEnhancedClient enhancedClient =
                DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();

        createTable(enhancedClient, "clients", Client.class);
        createTable(enhancedClient, "funds", Fund.class);
        createTable(enhancedClient, "subscriptions", Subscription.class);
        createTable(enhancedClient, "transactions", Transaction.class);
    }

    private <T> void createTable(DynamoDbEnhancedClient client,
                                 String tableName,
                                 Class<T> clazz) {

        DynamoDbTable<T> table =
                client.table(tableName, TableSchema.fromBean(clazz));

        try {
            table.createTable();
        } catch (Exception e) {
            System.out.println("Table already exist: " + tableName);
        }
    }
}
