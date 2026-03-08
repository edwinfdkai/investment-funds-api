package com.test.bank.repository;

import com.test.bank.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    void save(Transaction transaction);

    List<Transaction> findByClientId(String clientId);
}
