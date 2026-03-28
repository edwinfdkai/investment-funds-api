package com.test.bank.service;

import com.test.bank.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final SnsClient snsClient;

    @Override
    public void sendNotification(String message, String phone) {
        try {
            snsClient.publish(p -> p
                    .message(message)
                    .phoneNumber(phone)
            );

            log.info("SMS sent successfully to {}", phone);

        } catch (Exception e) {
            log.error("Error sending SMS to {}: {}", phone, e.getMessage(), e);
            throw new RuntimeException("Error sending SMS notification");
        }
    }
}
