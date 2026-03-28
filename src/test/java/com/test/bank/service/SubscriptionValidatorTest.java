package com.test.bank.service;

import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SubscriptionValidatorTest {

    private final SubscriptionValidator validator = new SubscriptionValidator();

    private Client client;
    private Fund fund;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId("c1");
        client.setBalance(1000L);

        fund = new Fund("f1", "Growth", 500L, "X");
    }

    @Test
    void validateNewSubscription_acceptsValidCase() {
        assertDoesNotThrow(() -> validator.validateNewSubscription(client, fund, null));
    }

    @Test
    void validateNewSubscription_rejectsMissingClient() {
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validateNewSubscription(null, fund, null));
        assertEquals("Client not found", ex.getMessage());
    }

    @Test
    void validateNewSubscription_rejectsMissingFund() {
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validateNewSubscription(client, null, null));
        assertEquals("Fund not found", ex.getMessage());
    }

    @Test
    void validateNewSubscription_rejectsDuplicateSubscription() {
        Subscription existing = new Subscription();
        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validateNewSubscription(client, fund, existing));
        assertEquals("Client already subscribed to this fund", ex.getMessage());
    }

    @Test
    void validateNewSubscription_rejectsInsufficientBalance() {
        client.setBalance(100L);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> validator.validateNewSubscription(client, fund, null));
        assertEquals(
                "Insufficient balance to subscribe to fund Growth",
                ex.getMessage());
    }
}
