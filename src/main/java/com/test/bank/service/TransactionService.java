package com.test.bank.service;

import com.test.bank.model.Transaction;
import com.test.bank.model.enums.TransactionType;

import java.util.List;

public interface TransactionService {

    void register(String clientId, String fundId, TransactionType type, Long amount);

    List<Transaction> history(String clientId);
}
