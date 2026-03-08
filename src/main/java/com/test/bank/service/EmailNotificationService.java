package com.test.bank.service;

import com.test.bank.repository.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String message, String email) {

        System.out.println("Sending EMAIL to " + email);
        System.out.println(message);
    }

}