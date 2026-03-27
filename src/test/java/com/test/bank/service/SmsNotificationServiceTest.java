package com.test.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsNotificationServiceTest {

    @Mock
    private SnsClient snsClient;

    @InjectMocks
    private SmsNotificationService smsNotificationService;

    @Test
    void shouldSendSmsSuccessfully() {
        String message = "Test message";
        String phone = "+57300123456";

        smsNotificationService.sendNotification(message, phone);

        verify(snsClient, times(1))
                .publish((Consumer<PublishRequest.Builder>) any());
    }

    @Test
    void shouldThrowExceptionWhenSmsFails() {
        String message = "Test message";
        String phone = "+57300123456";

        doThrow(new RuntimeException("AWS error"))
                .when(snsClient)
                .publish((PublishRequest) any());

        RuntimeException exception = org.junit.jupiter.api.Assertions.assertThrows(
                RuntimeException.class,
                () -> smsNotificationService.sendNotification(message, phone)
        );

        assert exception.getMessage().equals("Error sending SMS notification");
    }
}