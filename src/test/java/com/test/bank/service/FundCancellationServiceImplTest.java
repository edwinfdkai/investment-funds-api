package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Subscription;
import com.test.bank.model.enums.SubscriptionStatus;
import com.test.bank.model.enums.TransactionType;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundCancellationServiceImplTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private FundCancellationServiceImpl fundCancellationService;

    private Client testClient;
    private CancelRequest cancelRequest;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId("1");
        testClient.setBalance(1000L);

        cancelRequest = new CancelRequest();
        cancelRequest.setClientId("1");
        cancelRequest.setFundId("1");
    }

    @Test
    void cancel_marksSubscriptionCancelledRefundsAndRegistersTransaction() {
        Subscription subscription = new Subscription(
                "1", "1", 500L, SubscriptionStatus.ACTIVE.getValue(), LocalDate.now());

        when(subscriptionRepository.find("1", "1")).thenReturn(subscription);
        when(clientRepository.findById("1")).thenReturn(testClient);

        fundCancellationService.cancel(cancelRequest);

        assertEquals(SubscriptionStatus.CANCELLED.getValue(), subscription.getStatus());
        assertEquals(1500L, testClient.getBalance());
        verify(subscriptionRepository).save(subscription);
        verify(transactionService).register("1", "1", TransactionType.CANCELLATION, 500L);
    }

    @Test
    void cancel_rejectsWhenSubscriptionMissing() {
        when(subscriptionRepository.find("1", "1")).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundCancellationService.cancel(cancelRequest));
        assertEquals("Subscription not found", ex.getMessage());
    }

    @Test
    void cancel_rejectsWhenClientMissing() {
        Subscription subscription = new Subscription(
                "1", "1", 500L, SubscriptionStatus.ACTIVE.getValue(), LocalDate.now());

        when(subscriptionRepository.find("1", "1")).thenReturn(subscription);
        when(clientRepository.findById("1")).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundCancellationService.cancel(cancelRequest));
        assertEquals("Client not found", ex.getMessage());
    }
}
