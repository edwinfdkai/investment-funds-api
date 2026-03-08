package com.test.bank.service;

import com.test.bank.repository.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message, String phone) {

        System.out.println("Sending SMS to " + phone);
        System.out.println(message);
    }
}
