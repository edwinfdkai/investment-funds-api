package com.test.bank.repository.impl;

import com.test.bank.model.Subscription;
import com.test.bank.repository.SubscriptionRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;


@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final DynamoDbTable<Subscription> table;

    public SubscriptionRepositoryImpl(DynamoDbClient dynamoDbClient) {

        DynamoDbEnhancedClient enhancedClient =
                DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();

        this.table = enhancedClient.table(
                "subscriptions",
                TableSchema.fromBean(Subscription.class)
        );
    }

    @Override
    public void save(Subscription subscription) {
        table.putItem(subscription);
    }

    @Override
    public Subscription find(String clientId, String fundId) {

        return table.getItem(
                Key.builder()
                        .partitionValue(clientId)
                        .sortValue(fundId)
                        .build()
        );
    }
}
