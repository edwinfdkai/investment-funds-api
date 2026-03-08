package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.FundRepository;
import com.test.bank.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private FundRepository fundRepository;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private EmailNotificationService emailNotificationService;

    @Mock
    private SmsNotificationService smsNotificationService;

    @InjectMocks
    private FundService fundService;

    private Client testClient;
    private Fund testFund;
    private SubscriptionRequest subscriptionRequest;
    private CancelRequest cancelRequest;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setBalance(1000L);
        testClient.setEmail("test@email.com");
        testClient.setPhone("123456789");
        testClient.setNotificationPreference("EMAIL");

        testFund = new Fund(1L, "Test Fund", 500L, "Categoria");

        subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setClientId(1L);
        subscriptionRequest.setFundId(1L);

        cancelRequest = new CancelRequest();
        cancelRequest.setClientId(1L);
        cancelRequest.setFundId(1L);
    }


    @Test
    void subscribeToFund_Successful_WithEmailNotification() {
        when(clientRepository.findById(1L)).thenReturn(testClient);
        when(fundRepository.findById(1L)).thenReturn(testFund);
        when(subscriptionRepository.find(1L, 1L)).thenReturn(null);  // No existe suscripción previa

        fundService.subscribeToFund(subscriptionRequest);

        assertEquals(500L, testClient.getBalance());  // Saldo actualizado
        verify(subscriptionRepository).save(any(Subscription.class));
        verify(transactionService).register(1L, 1L, "OPENING", 500L);
        verify(emailNotificationService).sendNotification(anyString(), eq("test@email.com"));
        verify(smsNotificationService, never()).sendNotification(anyString(), anyString());  // No se llama SMS
    }

    @Test
    void subscribeToFund_Successful_WithSmsNotification() {
        testClient.setNotificationPreference("SMS");  // Cambia preferencia

        when(clientRepository.findById(1L)).thenReturn(testClient);
        when(fundRepository.findById(1L)).thenReturn(testFund);
        when(subscriptionRepository.find(1L, 1L)).thenReturn(null);

        fundService.subscribeToFund(subscriptionRequest);

        verify(smsNotificationService).sendNotification(anyString(), eq("123456789"));
        verify(emailNotificationService, never()).sendNotification(anyString(), anyString());
    }

    @Test
    void subscribeToFund_ThrowsException_ClientNotFound() {
        when(clientRepository.findById(1L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> fundService.subscribeToFund(subscriptionRequest));
        assertEquals("Client not found", exception.getMessage());
    }

    @Test
    void subscribeToFund_ThrowsException_FundNotFound() {
        when(clientRepository.findById(1L)).thenReturn(testClient);
        when(fundRepository.findById(1L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> fundService.subscribeToFund(subscriptionRequest));
        assertEquals("Fund not found", exception.getMessage());
    }

    @Test
    void subscribeToFund_ThrowsException_AlreadySubscribed() {
        when(clientRepository.findById(1L)).thenReturn(testClient);
        when(fundRepository.findById(1L)).thenReturn(testFund);
        when(subscriptionRepository.find(1L, 1L)).thenReturn(new Subscription());  // Suscripción existente

        BusinessException exception = assertThrows(BusinessException.class, () -> fundService.subscribeToFund(subscriptionRequest));
        assertEquals("Client already subscribed to this fund", exception.getMessage());
    }

    @Test
    void subscribeToFund_ThrowsException_InsufficientBalance() {
        testClient.setBalance(400L);  // Menos que minAmount (500)

        when(clientRepository.findById(1L)).thenReturn(testClient);
        when(fundRepository.findById(1L)).thenReturn(testFund);
        when(subscriptionRepository.find(1L, 1L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> fundService.subscribeToFund(subscriptionRequest));
        assertTrue(exception.getMessage().contains("Insufficient balance"));
    }


    @Test
    void cancelFund_Successful() {
        Subscription testSubscription = new Subscription(1L, 1L, 500L, "ACTIVE", LocalDate.now());

        when(subscriptionRepository.find(1L, 1L)).thenReturn(testSubscription);
        when(clientRepository.findById(1L)).thenReturn(testClient);

        fundService.cancelFund(cancelRequest);

        assertEquals("CANCELLED", testSubscription.getStatus());
        assertEquals(1500L, testClient.getBalance());
        verify(transactionService).register(1L, 1L, "CANCELLATION", 500L);
    }

    @Test
    void cancelFund_ThrowsException_SubscriptionNotFound() {
        when(subscriptionRepository.find(1L, 1L)).thenReturn(null);

        assertThrows(NullPointerException.class, () -> fundService.cancelFund(cancelRequest));
    }


    @Test
    void getFunds_ReturnsList() {
        List<Fund> expectedFunds = List.of(testFund);
        when(fundRepository.findAll()).thenReturn(expectedFunds);

        List<Fund> result = fundService.getFunds();

        assertEquals(1, result.size());
        assertEquals("Test Fund", result.get(0).getName());
    }
}