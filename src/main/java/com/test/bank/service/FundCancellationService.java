package com.test.bank.service;

import com.test.bank.dto.CancelRequest;

public interface FundCancellationService {

    void cancel(CancelRequest request);
}
