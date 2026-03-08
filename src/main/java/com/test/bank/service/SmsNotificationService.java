package com.test.bank.service;

import com.test.bank.repository.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@RequiredArgsConstructor
public class SmsNotificationService implements NotificationService {

    private final SnsClient snsClient;

    @Override
    public void sendNotification(String message, String phone) {
        try {
            snsClient.publish(p -> p.message(message).phoneNumber(phone));
            System.out.println("SMS send to: " + phone);
        } catch (Exception e) {
            System.err.println("Error send SMS: " + e.getMessage());
        }
    }
}
