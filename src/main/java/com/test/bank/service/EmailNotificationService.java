package com.test.bank.service;

import com.test.bank.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationService {

    private final SesClient sesClient;

    @Override
    public void sendNotification(String message, String email) {
        try {
            sesClient.sendEmail(e -> e
                    .destination(d -> d.toAddresses(email))
                    .message(m -> m
                            .subject(s -> s.data("Update funds"))
                            .body(b -> b.text(t -> t.data(message))))
                    .source("edwinkaicedosoporte@gmail.com")
            );
            log.info("Email sent successfully to {}", email);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", email, e.getMessage(), e);
        }
    }
}
