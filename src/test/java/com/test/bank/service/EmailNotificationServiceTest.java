package com.test.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private SesClient sesClient;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    void sendNotification_invokesSesSendEmail() {
        emailNotificationService.sendNotification("Test message", "test@mail.com");

        verify(sesClient).sendEmail(any(Consumer.class));
    }

    @Test
    void sendNotification_swallowsSesFailureAfterLogging() {
        doThrow(new RuntimeException("AWS error"))
                .when(sesClient)
                .sendEmail(any(Consumer.class));

        emailNotificationService.sendNotification("Test message", "test@mail.com");

        verify(sesClient).sendEmail(any(Consumer.class));
    }
}
