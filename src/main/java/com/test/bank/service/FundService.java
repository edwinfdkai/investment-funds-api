package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.model.Fund;

import java.util.List;

public interface FundService {

    void subscribeToFund(SubscriptionRequest request);

    void cancelFund(CancelRequest request);

    List<Fund> getFunds();
}
