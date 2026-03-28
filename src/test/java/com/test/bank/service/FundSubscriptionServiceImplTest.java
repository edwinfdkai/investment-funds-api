package com.test.bank.service;

import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import com.test.bank.model.enums.NotificationPreference;
import com.test.bank.model.enums.TransactionType;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.FundRepository;
import com.test.bank.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FundSubscriptionServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionNotifier subscriptionNotifier;

    @Mock
    private TransactionService transactionService;

    private final SubscriptionValidator subscriptionValidator = new SubscriptionValidator();

    private FundSubscriptionServiceImpl fundSubscriptionService;

    private Client testClient;
    private Fund testFund;
    private SubscriptionRequest subscriptionRequest;

    @BeforeEach
    void setUp() {
        fundSubscriptionService = new FundSubscriptionServiceImpl(
                clientRepository,
                fundRepository,
                subscriptionRepository,
                subscriptionValidator,
                subscriptionNotifier,
                transactionService);

        testClient = new Client();
        testClient.setId("1");
        testClient.setBalance(1000L);
        testClient.setEmail("test@email.com");
        testClient.setPhone("123456789");
        testClient.setNotificationPreference(NotificationPreference.EMAIL.getValue());

        testFund = new Fund("1", "Test Fund", 500L, "Categoria");

        subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setClientId("1");
        subscriptionRequest.setFundId("1");
    }

    @Test
    void subscribe_debitsClientSavesSubscriptionRegistersTransactionAndNotifies() {
        when(clientRepository.findById("1")).thenReturn(testClient);
        when(fundRepository.findById("1")).thenReturn(testFund);
        when(subscriptionRepository.find("1", "1")).thenReturn(null);

        fundSubscriptionService.subscribe(subscriptionRequest);

        assertEquals(500L, testClient.getBalance());
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(transactionService).register("1", "1", TransactionType.OPENING, 500L);
        verify(subscriptionNotifier).notifySubscriptionCreated(testClient, testFund);
    }

    @Test
    void subscribe_notifiesWhenPreferenceIsSms() {
        testClient.setNotificationPreference(NotificationPreference.SMS.getValue());

        when(clientRepository.findById("1")).thenReturn(testClient);
        when(fundRepository.findById("1")).thenReturn(testFund);
        when(subscriptionRepository.find("1", "1")).thenReturn(null);

        fundSubscriptionService.subscribe(subscriptionRequest);

        verify(subscriptionNotifier).notifySubscriptionCreated(testClient, testFund);
    }

    @Test
    void subscribe_rejectsWhenClientMissing() {
        when(clientRepository.findById("1")).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundSubscriptionService.subscribe(subscriptionRequest));
        assertEquals("Client not found", ex.getMessage());
    }

    @Test
    void subscribe_rejectsWhenFundMissing() {
        when(clientRepository.findById("1")).thenReturn(testClient);
        when(fundRepository.findById("1")).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundSubscriptionService.subscribe(subscriptionRequest));
        assertEquals("Fund not found", ex.getMessage());
    }

    @Test
    void subscribe_rejectsWhenAlreadySubscribed() {
        when(clientRepository.findById("1")).thenReturn(testClient);
        when(fundRepository.findById("1")).thenReturn(testFund);
        when(subscriptionRepository.find("1", "1")).thenReturn(new Subscription());

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundSubscriptionService.subscribe(subscriptionRequest));
        assertEquals("Client already subscribed to this fund", ex.getMessage());
    }

    @Test
    void subscribe_rejectsWhenInsufficientBalance() {
        testClient.setBalance(400L);

        when(clientRepository.findById("1")).thenReturn(testClient);
        when(fundRepository.findById("1")).thenReturn(testFund);
        when(subscriptionRepository.find("1", "1")).thenReturn(null);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> fundSubscriptionService.subscribe(subscriptionRequest));
        assertTrue(ex.getMessage().contains("Insufficient balance"));
    }
}
