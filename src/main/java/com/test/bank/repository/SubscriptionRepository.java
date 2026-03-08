package com.test.bank.repository;

import com.test.bank.model.Subscription;

public interface SubscriptionRepository {

    void save(Subscription subscription);

    Subscription find(String clientId, String fundId);
}
