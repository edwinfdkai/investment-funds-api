package com.test.bank.service;

import com.test.bank.dto.CancelRequest;
import com.test.bank.dto.SubscriptionRequest;
import com.test.bank.exception.BusinessException;
import com.test.bank.model.Client;
import com.test.bank.model.Fund;
import com.test.bank.model.Subscription;
import com.test.bank.repository.ClientRepository;
import com.test.bank.repository.FundRepository;
import com.test.bank.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FundService {


    private final ClientRepository clientRepository;
    private final FundRepository fundRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TransactionService transactionService;
    private final EmailNotificationService emailNotificationService;
    private final SmsNotificationService smsNotificationService;

    public void subscribeToFund(SubscriptionRequest request) {

        Client client = clientRepository.findById(request.getClientId());

        Fund fund = fundRepository.findById(request.getFundId());

        if(client == null){
            throw new BusinessException("Client not found");
        }

        if(fund == null){
            throw new BusinessException("Fund not found");
        }

        Subscription existing = subscriptionRepository
                .find(request.getClientId(), request.getFundId());

        if(existing != null){
            throw new BusinessException("Client already subscribed to this fund");
        }

        if (client.getBalance() < fund.getMinAmount()) {

            throw new BusinessException(
                    "Insufficient balance to subscribe to fund "
                            + fund.getName());
        }

        client.setBalance(client.getBalance() - fund.getMinAmount());

        Subscription subscription = new Subscription(
                client.getId(),
                fund.getId(),
                fund.getMinAmount(),
                "ACTIVE",
                LocalDate.now()
        );

        if(client.getNotificationPreference().equals("EMAIL")){

            emailNotificationService.sendNotification(
                    "You have subscribed to fund " + fund.getName(),
                    client.getEmail()
            );

        }else{

            smsNotificationService.sendNotification(
                    "You have subscribed to fund " + fund.getName(),
                    client.getPhone()
            );
        }

        subscriptionRepository.save(subscription);

        transactionService.register(
                client.getId(),
                fund.getId(),
                "OPENING",
                fund.getMinAmount()
        );
    }

    public void cancelFund(CancelRequest request) {

        Subscription subscription = subscriptionRepository.find(
                request.getClientId(),
                request.getFundId());

        subscription.setStatus("CANCELLED");

        Client client = clientRepository.findById(request.getClientId());

        client.setBalance(client.getBalance() + subscription.getAmount());

        transactionService.register(
                client.getId(),
                request.getFundId(),
                "CANCELLATION",
                subscription.getAmount()
        );
    }

    public List<Fund> getFunds(){
        return fundRepository.findAll();
    }
}
