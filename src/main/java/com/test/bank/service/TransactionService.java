package com.test.bank.service;

import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public void register(String clientId, String fundId, String type, Long amount){

        Transaction transaction = new Transaction();

        transaction.setId(UUID.randomUUID().toString());
        transaction.setClientId(clientId);
        transaction.setFundId(fundId);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setDate(LocalDate.now());

        transactionRepository.save(transaction);
    }

    public List<Transaction> history(String clientId){
        return transactionRepository.findByClientId(clientId);
    }
}
