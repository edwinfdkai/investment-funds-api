package com.test.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailNotificationServiceTest {

    @Mock
    private SesClient sesClient;

    @InjectMocks
    private EmailNotificationService emailNotificationService;

    @Test
    void shouldSendEmailSuccessfully() {
        String message = "Test message";
        String email = "test@mail.com";

        emailNotificationService.sendNotification(message, email);

        verify(sesClient, times(1))
                .sendEmail((Consumer<SendEmailRequest.Builder>) any());
    }

    @Test
    void shouldHandleExceptionWhenEmailFails() {
        String message = "Test message";
        String email = "test@mail.com";

        doThrow(new RuntimeException("AWS error"))
                .when(sesClient)
                .sendEmail((Consumer<SendEmailRequest.Builder>) any());

        emailNotificationService.sendNotification(message, email);

        verify(sesClient, times(1))
                .sendEmail((Consumer<SendEmailRequest.Builder>) any());
    }
}