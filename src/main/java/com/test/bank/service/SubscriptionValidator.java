package com.test.bank.service;

import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionValidator {

    public void validateNewSubscription(Client client, Fund fund, Subscription existing) {
        if (client == null) {
            throw new BusinessException("Client not found");
        }
        if (fund == null) {
            throw new BusinessException("Fund not found");
        }
        if (existing != null) {
            throw new BusinessException("Client already subscribed to this fund");
        }
        if (client.getBalance() < fund.getMinAmount()) {
            throw new BusinessException(
                    "Insufficient balance to subscribe to fund " + fund.getName());
        }
    }
}
