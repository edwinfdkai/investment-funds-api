package com.test.bank.repository.impl;

import com.test.bank.model.Client;
import com.test.bank.repository.ClientRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;



@Repository
public class ClientRepositoryImpl implements ClientRepository {

    private final DynamoDbEnhancedClient enhancedClient;
    private final DynamoDbTable<Client> table;

    public ClientRepositoryImpl(DynamoDbClient dynamoDbClient) {
        this.enhancedClient = DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();

        this.table = enhancedClient.table(
                "clients",
                TableSchema.fromBean(Client.class)
        );
    }

    @Override
    public Client findById(String id) {
        return table.getItem(Key.builder().partitionValue(id).build());
    }

    @Override
    public void save(Client client) {
        table.putItem(client);
    }
}