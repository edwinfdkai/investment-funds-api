package com.test.bank.service;

import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.enums.NotificationPreference;
import com.test.bank.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class SubscriptionNotifierImplTest {

    @Mock
    private NotificationService emailNotificationService;

    @Mock
    private NotificationService smsNotificationService;

    private SubscriptionNotifierImpl subscriptionNotifier;

    private Client client;
    private Fund fund;

    @BeforeEach
    void setUp() {
        subscriptionNotifier = new SubscriptionNotifierImpl(emailNotificationService, smsNotificationService);

        client = new Client();
        client.setEmail("a@b.com");
        client.setPhone("555");
        fund = new Fund("f1", "Growth", 100L, "X");
    }

    @Test
    void notifySubscriptionCreated_usesEmailWhenPreferenceIsEmail() {
        client.setNotificationPreference(NotificationPreference.EMAIL.getValue());

        subscriptionNotifier.notifySubscriptionCreated(client, fund);

        verify(emailNotificationService).sendNotification(
                eq("You have subscribed to fund Growth"),
                eq("a@b.com"));
        verifyNoInteractions(smsNotificationService);
    }

    @Test
    void notifySubscriptionCreated_usesSmsWhenPreferenceIsSms() {
        client.setNotificationPreference(NotificationPreference.SMS.getValue());

        subscriptionNotifier.notifySubscriptionCreated(client, fund);

        verify(smsNotificationService).sendNotification(
                eq("You have subscribed to fund Growth"),
                eq("555"));
        verifyNoInteractions(emailNotificationService);
    }

    @Test
    void notifySubscriptionCreated_throwsWhenPreferenceInvalid() {
        client.setNotificationPreference("PUSH");

        assertThrows(BusinessException.class, () -> subscriptionNotifier.notifySubscriptionCreated(client, fund));
        verifyNoInteractions(emailNotificationService, smsNotificationService);
    }
}
