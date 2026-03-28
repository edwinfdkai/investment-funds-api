package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.model.Fund;
import com.test.bank.repository.FundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FundServiceImpl implements FundService {

    private final FundSubscriptionService fundSubscriptionService;
    private final FundCancellationService fundCancellationService;
    private final FundRepository fundRepository;

    @Override
    public void subscribeToFund(SubscriptionRequest request) {
        fundSubscriptionService.subscribe(request);
    }

    @Override
    public void cancelFund(CancelRequest request) {
        fundCancellationService.cancel(request);
    }

    @Override
    public List<Fund> getFunds() {
        return fundRepository.findAll();
    }
}
