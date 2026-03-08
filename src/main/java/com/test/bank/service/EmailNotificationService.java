package com.test.bank.service;

import com.test.bank.repository.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;

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
            System.out.println("Email send to: " + email);
        } catch (Exception e) {
            System.err.println("Error send Email: " + e.getMessage());
        }
    }
}