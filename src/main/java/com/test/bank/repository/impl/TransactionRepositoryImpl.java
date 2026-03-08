package com.test.bank.repository.impl;

import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findByClientId(Long clientId) {

        return transactions.stream()
                .filter(t -> t.getClientId().equals(clientId))
                .toList();
    }
}
