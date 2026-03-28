package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Subscription;
import com.test.bank.model.enums.SubscriptionStatus;
import com.test.bank.model.enums.TransactionType;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FundCancellationServiceImpl implements FundCancellationService {

    private final SubscriptionRepository subscriptionRepository;
    private final ClientRepository clientRepository;
    private final TransactionService transactionService;

    @Override
    public void cancel(CancelRequest request) {
        Subscription subscription = subscriptionRepository.find(
                request.getClientId(),
                request.getFundId());

        if (subscription == null) {
            throw new BusinessException("Subscription not found");
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED.getValue());
        subscriptionRepository.save(subscription);

        Client client = clientRepository.findById(request.getClientId());
        if (client == null) {
            throw new BusinessException("Client not found");
        }
        client.setBalance(client.getBalance() + subscription.getAmount());
        clientRepository.save(client);

        transactionService.register(
                client.getId(),
                request.getFundId(),
                TransactionType.CANCELLATION,
                subscription.getAmount());
    }
}
