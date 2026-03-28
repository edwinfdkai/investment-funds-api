package com.test.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sns.SnsClient;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {

    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private SmsNotificationService smsNotificationService;

    @Test
    void sendNotification_invokesSnsPublish() {
        smsNotificationService.sendNotification("Test message", "+57300123456");

        verify(snsClient).publish(any(Consumer.class));
    }

    @Test
    void sendNotification_wrapsFailureInRuntimeException() {
        doThrow(new RuntimeException("AWS error"))
                .when(snsClient)
                .publish(any(Consumer.class));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> smsNotificationService.sendNotification("Test message", "+57300123456"));

        assertEquals("Error sending SMS notification", exception.getMessage());
        verify(snsClient).publish(any(Consumer.class));
    }
}
