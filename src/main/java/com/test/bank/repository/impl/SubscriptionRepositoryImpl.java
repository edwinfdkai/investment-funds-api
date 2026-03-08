package com.test.bank.repository.impl;

import com.test.bank.model.Subscription;
import com.test.bank.repository.SubscriptionRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final Map<String, Subscription> subscriptions = new HashMap<>();

    @Override
    public void save(Subscription subscription) {
        String key = subscription.getClientId() + "-" + subscription.getFundId();
        subscriptions.put(key, subscription);
    }

    @Override
    public Subscription find(Long clientId, Long fundId) {
        return subscriptions.get(clientId + "-" + fundId);
    }
}
