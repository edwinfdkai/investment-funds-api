package com.test.bank.service;

import com.test.bank.model.Client;
import com.test.bank.model.Fund;

public interface SubscriptionNotifier {

    void notifySubscriptionCreated(Client client, Fund fund);
}
