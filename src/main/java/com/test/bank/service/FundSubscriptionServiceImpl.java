package com.test.bank.service;

import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import com.test.bank.model.enums.SubscriptionStatus;
import com.test.bank.model.enums.TransactionType;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.FundRepository;
import com.test.bank.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FundSubscriptionServiceImpl implements FundSubscriptionService {

    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionValidator subscriptionValidator;
    private final SubscriptionNotifier subscriptionNotifier;
    private final TransactionService transactionService;

    @Override
    public void subscribe(SubscriptionRequest request) {
        Client client = clientRepository.findById(request.getClientId());
        Fund fund = fundRepository.findById(request.getFundId());
        Subscription existing = subscriptionRepository.find(request.getClientId(), request.getFundId());

        subscriptionValidator.validateNewSubscription(client, fund, existing);

        client.setBalance(client.getBalance() - fund.getMinAmount());
        clientRepository.save(client);

        Subscription subscription = new Subscription(
                client.getId(),
                fund.getId(),
                fund.getMinAmount(),
                SubscriptionStatus.ACTIVE.getValue(),
                LocalDate.now()
        );
        subscriptionRepository.save(subscription);

        subscriptionNotifier.notifySubscriptionCreated(client, fund);

        transactionService.register(
                client.getId(),
                fund.getId(),
                TransactionType.OPENING,
                fund.getMinAmount());
    }
}
