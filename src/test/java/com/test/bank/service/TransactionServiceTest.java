package com.test.bank.service;

import com.test.bank.model.Transaction;
import com.test.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private String testClientId;
    private String testFundId;
    private String testType;
    private Long testAmount;

    @BeforeEach
    void setUp() {
        testClientId = "1L";
        testFundId = "1L";
        testType = "OPENING";
        testAmount = 500L;
    }


    @Test
    void register_Successful() {
        transactionService.register(testClientId, testFundId, testType, testAmount);

        verify(transactionRepository).save(any(Transaction.class));

        verify(transactionRepository).save(argThat(transaction ->
                transaction.getClientId().equals(testClientId) &&
                        transaction.getFundId().equals(testFundId) &&
                        transaction.getType().equals(testType) &&
                        transaction.getAmount().equals(testAmount) &&
                        transaction.getDate().equals(LocalDate.now()) &&
                        transaction.getId() != null && !transaction.getId().isEmpty()
        ));
    }


    @Test
    void history_ReturnsTransactions() {
        Transaction testTransaction = new Transaction(
                UUID.randomUUID().toString(),
                testClientId,
                testFundId,
                testType,
                testAmount,
                LocalDate.now()
        );
        List<Transaction> expectedTransactions = List.of(testTransaction);
        when(transactionRepository.findByClientId(testClientId)).thenReturn(expectedTransactions);

        List<Transaction> result = transactionService.history(testClientId);

        assertEquals(1, result.size());
        assertEquals(testClientId, result.get(0).getClientId());
        assertEquals(testType, result.get(0).getType());
    }

    @Test
    void history_ReturnsEmptyList_WhenNoTransactions() {
        when(transactionRepository.findByClientId(testClientId)).thenReturn(List.of());

        List<Transaction> result = transactionService.history(testClientId);

        assertTrue(result.isEmpty());
    }
}