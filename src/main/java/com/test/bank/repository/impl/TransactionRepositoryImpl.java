package com.test.bank.repository.impl;

import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final DynamoDbTable<Transaction> table;

    public TransactionRepositoryImpl(DynamoDbClient dynamoDbClient) {

        DynamoDbEnhancedClient enhancedClient =
                DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();

        this.table = enhancedClient.table(
                "transactions",
                TableSchema.fromBean(Transaction.class)
        );
    }

    @Override
    public void save(Transaction transaction) {
        table.putItem(transaction);
    }

    @Override
    public List<Transaction> findByClientId(String clientId) {

        List<Transaction> result = new ArrayList<>();

        table.scan().items()
                .stream()
                .filter(t -> t.getClientId().equals(clientId))
                .forEach(result::add);

        return result;
    }
}
