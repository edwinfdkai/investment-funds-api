package com.test.bank.service;

import com.test.bank.model.Transaction;
import com.test.bank.model.enums.TransactionType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private String testClientId;
    private String testFundId;
    private TransactionType testType;
    private Long testAmount;

    @BeforeEach
    void setUp() {
        testClientId = "1L";
        testFundId = "1L";
        testType = TransactionType.OPENING;
        testAmount = 500L;
    }

    @Test
    void register_persistsTransactionWithExpectedFields() {
        transactionService.register(testClientId, testFundId, testType, testAmount);

        verify(transactionRepository).save(argThat(transaction ->
                transaction.getClientId().equals(testClientId)
                        && transaction.getFundId().equals(testFundId)
                        && transaction.getType().equals(testType.getValue())
                        && transaction.getAmount().equals(testAmount)
                        && transaction.getDate().equals(LocalDate.now())
                        && transaction.getId() != null
                        && !transaction.getId().isEmpty()));
    }

    @Test
    void history_returnsTransactionsFromRepository() {
        Transaction stored = new Transaction(
                UUID.randomUUID().toString(),
                testClientId,
                testFundId,
                testType.getValue(),
                testAmount,
                LocalDate.now());
        when(transactionRepository.findByClientId(testClientId)).thenReturn(List.of(stored));

        List<Transaction> result = transactionService.history(testClientId);

        assertEquals(1, result.size());
        assertEquals(testClientId, result.get(0).getClientId());
        assertEquals(testType.getValue(), result.get(0).getType());
    }

    @Test
    void history_returnsEmptyWhenNoTransactions() {
        when(transactionRepository.findByClientId(testClientId)).thenReturn(List.of());

        List<Transaction> result = transactionService.history(testClientId);

        assertTrue(result.isEmpty());
    }
}
