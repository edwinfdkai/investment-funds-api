package com.test.bank.service;

import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.enums.NotificationPreference;
import com.test.bank.notification.NotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionNotifierImpl implements SubscriptionNotifier {

    private final NotificationService emailNotificationService;
    private final NotificationService smsNotificationService;

    public SubscriptionNotifierImpl(
            @Qualifier("emailNotificationService") NotificationService emailNotificationService,
            @Qualifier("smsNotificationService") NotificationService smsNotificationService) {
        this.emailNotificationService = emailNotificationService;
        this.smsNotificationService = smsNotificationService;
    }

    @Override
    public void notifySubscriptionCreated(Client client, Fund fund) {
        String message = "You have subscribed to fund " + fund.getName();

        NotificationPreference preference;
        try {
            preference = NotificationPreference.fromStoredValue(client.getNotificationPreference());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid or unsupported notification preference");
        }

        if (preference == NotificationPreference.EMAIL) {
            emailNotificationService.sendNotification(message, client.getEmail());
        } else {
            smsNotificationService.sendNotification(message, client.getPhone());
        }
    }
}
