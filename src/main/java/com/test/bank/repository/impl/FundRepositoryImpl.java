package com.test.bank.repository.impl;


import com.test.bank.model.Fund;
import com.test.bank.repository.FundRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FundRepositoryImpl implements FundRepository {

    private final DynamoDbTable<Fund> table;

    public FundRepositoryImpl(DynamoDbClient dynamoDbClient) {

        DynamoDbEnhancedClient enhancedClient =
                DynamoDbEnhancedClient.builder()
                        .dynamoDbClient(dynamoDbClient)
                        .build();

        this.table = enhancedClient.table(
                "funds",
                TableSchema.fromBean(Fund.class)
        );
    }

    @Override
    public Fund findById(String id) {
        return table.getItem(
                Key.builder().partitionValue(id).build()
        );
    }

    @Override
    public void save(Fund fund) {
        table.putItem(fund);
    }

    @Override
    public List<Fund> findAll() {

        List<Fund> result = new ArrayList<>();

        table.scan().items().forEach(result::add);

        return result;
    }
}
